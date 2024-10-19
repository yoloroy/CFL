package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.lexer.NumberLiteral
import com.yoloroy.lexer.TextLiteral
import com.yoloroy.parser.util.subList
import com.yoloroy.lexer.Identifier as LexerIdentifier

private sealed class Priority(val i: Int) : Comparable<Priority> {
    data object Value : Priority(1)
    data object PostfixUnary : Priority(2)
    data object Logic : Priority(3)
    data object Comparison : Priority(4)
    data object PlusMinus : Priority(5)
    data object MulDiv : Priority(6)
    data object PrefixUnary : Priority(7)
    data object CollectionOperation : Priority(8)
    data object SwitchCase : Priority(9)
    data object Assign : Priority(10)
    data object Generator : Priority(11)
    data object Parenthesis : Priority(12)
    data class SwitchCaseCases(val lexeme: Lexeme) : Priority(13)
    data object Comment : Priority(14)

    override fun compareTo(other: Priority): Int = i.compareTo(other.i)
}

/**
 * Lesser the index — higher in parsing order
 */
private fun parsingPriorityOf(lexeme: Lexeme): Priority = when (lexeme) {

    Keyword.Map -> Priority.CollectionOperation

    Keyword.Assign -> Priority.Assign

    Keyword.SwitchCase -> Priority.SwitchCase

    Keyword.SwitchCaseCase, Keyword.SwitchCaseElse -> Priority.SwitchCaseCases(lexeme)

    Keyword.First -> Priority.PostfixUnary

    Keyword.Count -> Priority.PrefixUnary

    Keyword.Comment -> Priority.Comment

    Keyword.OpenParen, Keyword.CloseParen -> Priority.Parenthesis

    Keyword.Mul, Keyword.Div, Keyword.Cast, Keyword.CastOrNull -> Priority.MulDiv

    Keyword.Plus, Keyword.Minus, Keyword.Mod -> Priority.PlusMinus

    Keyword.Equals, Keyword.NotEquals -> Priority.Logic

    Keyword.Generator -> Priority.Generator

    is NumberLiteral, is TextLiteral, is LexerIdentifier, is Expression -> Priority.Value

    else -> throw ExpressionParsingException("This lexeme ($lexeme of ${lexeme::class}) cannot be a part of expression")
}

fun Expression.Companion.from(lexemes: List<Lexeme>) : Expression {
    if (lexemes.last() == Keyword.EndLine) {
        return Expression.from(lexemes.dropLastWhile { it == Keyword.EndLine })
    }

    if (lexemes.size == 1 && lexemes[0] is Expression) {
        return lexemes[0] as Expression
    }

    return when (val priority = lexemes.asSequence().map(::parsingPriorityOf).max()) {
        Priority.Value -> {
            lexemes.singleOrNull()
                ?.let { Value.from(it) }
                ?: throw ExpressionParsingException("Values cannot just stand one after another")
        }
        Priority.PostfixUnary -> parsePostfixUnaryOperatorFrom(lexemes)
        Priority.Logic -> BinaryOperator.from(lexemes, Keyword.Equals, Keyword.NotEquals)
        Priority.Comparison -> TODO()
        Priority.PlusMinus -> BinaryOperator.from(lexemes, Keyword.Plus, Keyword.Minus, Keyword.Mod)
        Priority.MulDiv -> BinaryOperator.from(lexemes, Keyword.Mul, Keyword.Div, Keyword.Cast, Keyword.CastOrNull)
        Priority.CollectionOperation -> CollectionOperation.from(lexemes)
        Priority.PrefixUnary -> parsePrefixUnaryOperatorFrom(lexemes)
        Priority.SwitchCase -> SwitchCase.from(lexemes)
        Priority.Assign -> Assignment.from(lexemes)
        Priority.Generator -> Generator.from(lexemes)
        Priority.Parenthesis -> parseParenthesis(lexemes)
        is Priority.SwitchCaseCases -> when (val lexeme = priority.lexeme) {
            Keyword.SwitchCaseCase -> SwitchCase.ParametrizedCase.from(lexemes)
            Keyword.SwitchCaseElse -> SwitchCase.ElseCase.from(lexemes)
            else -> throw ExpressionParsingException("This lexeme (`$lexeme` of ${lexeme::class}) is not a case in switch case")
        }
        Priority.Comment -> parseCommentLine(lexemes)
    }
}

internal fun parseParenthesis(lexemes: List<Lexeme>): Expression {
    val openI = lexemes.indexOfFirst { it == Keyword.OpenParen }

    var count = 0
    val closeI = lexemes.indexOfFirst close@ {
        when (it) {
            Keyword.OpenParen -> count++
            Keyword.CloseParen -> return@close --count == 0
        }
        return@close false
    }

    if (openI == -1 && closeI == -1) {
        throw UnknownError("This cannot happen")
    }
    if (openI == -1) {
        throw ExpressionParsingException("There are no opening parenthesis")
    }
    if (closeI == -1) {
        throw ExpressionParsingException(
            when {
                count > 0 -> "Parenthesis are not closing"
                else /*count < 0*/ -> "Parentheses closing nothing"
            }
        )
    }
    if (closeI - openI == 1) {
        throw ExpressionParsingException("These parenthesis have nothing inside")
    }

    // FIXME: Is this prevents good unit-testing?
    return Expression.from(
        lexemes.subList(0 ..< openI) +
        Expression.from(lexemes.subList(openI + 1 ..< closeI)) +
        lexemes.subList(closeI + 1 .. lexemes.lastIndex)
    )
}

sealed interface Expression : Lexeme {
    companion object
}

