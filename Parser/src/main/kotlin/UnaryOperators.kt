package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

// TODO sealed class
internal val postfixUnaryOperatorConstructors = mapOf<Lexeme, (List<Lexeme>) -> Expression>(
    Keyword.First to FirstOperator::from
)
internal val prefixUnaryOperatorConstructors = mapOf<Lexeme, (List<Lexeme>) -> Expression>(
    Keyword.Count to CountOperator::from,
    Keyword.Comment to ::parseCommentLine
)

fun parsePostfixUnaryOperatorFrom(lexemes: List<Lexeme>): Expression = parseUnaryOperator(lexemes, postfixUnaryOperatorConstructors)

fun parsePrefixUnaryOperatorFrom(lexemes: List<Lexeme>): Expression = parseUnaryOperator(lexemes, prefixUnaryOperatorConstructors)

private fun parseUnaryOperator(lexemes: List<Lexeme>, constructors: Map<Lexeme, (List<Lexeme>) -> Expression>): Expression {
    // TODO refactor
    return lexemes
        .firstOrNull { it in constructors }
        ?.let { constructors[it] }
        ?.invoke(lexemes)
        ?: throw ExpressionParsingException()
}

fun FirstOperator.Companion.from(lexemes: List<Lexeme>): FirstOperator {
    return FirstOperator(Expression.from(lexemes.dropLast(1)))
}

fun CountOperator.Companion.from(lexemes: List<Lexeme>): Expression {
    val beforeCount = lexemes.takeWhile { it != Keyword.Count }
    return Expression.from(
        beforeCount +
        CountOperator(Expression.from(lexemes.subList(beforeCount.lastIndex + 2 .. lexemes.lastIndex)))
    )
}

fun parseCommentLine(lexemes: List<Lexeme>): Expression {
    val commentI = lexemes.indexOfFirst { it == Keyword.Comment }
    return Expression.from(lexemes.subList(0 ..< commentI))
}

data class FirstOperator(val stream: Expression) : Expression { companion object }

data class CountOperator(val stream: Expression) : Expression { companion object }
