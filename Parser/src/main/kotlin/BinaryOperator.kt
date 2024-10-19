package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun BinaryOperator.Companion.from(lexemes: List<Lexeme>, vararg operators: Keyword): Expression {
    val i = lexemes
        .indexOfFirst { it in operators }
        .takeIf { it != -1 }
        ?: throw UnknownError("This cannot happen")

    return BinaryOperator(
        lexemes[i] as Keyword,
        Expression.from(lexemes.subList(0 ..< i)),
        Expression.from(lexemes.subList(i + 1 .. lexemes.lastIndex))
    )
}

sealed class BinaryOperator(val left: Expression, val right: Expression) : Expression {
    companion object {
        operator fun invoke(keyword: Keyword, left: Expression, right: Expression) = when (keyword) {
            Keyword.Plus -> Plus(left, right)
            Keyword.Minus -> Minus(left, right)
            Keyword.Mul -> Multiply(left, right)
            Keyword.Div -> Divide(left, right)
            Keyword.Cast -> Cast(left, right)
            Keyword.CastOrNull -> CastOrNull(left, right)
            Keyword.Mod -> Mod(left, right)
            Keyword.Equals -> Equals(left, right)
            Keyword.NotEquals -> NotEquals(left, right)
            else -> throw ExpressionParsingException("There is no such operator")
        }
    }

    class Plus(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Minus(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Multiply(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Divide(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Cast(left: Expression, right: Expression) : BinaryOperator(left, right)
    class CastOrNull(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Mod(left: Expression, right: Expression) : BinaryOperator(left, right)
    class Equals(left: Expression, right: Expression) : BinaryOperator(left, right)
    class NotEquals(left: Expression, right: Expression) : BinaryOperator(left, right)

    override fun equals(other: Any?): Boolean = other != null && other is BinaryOperator && other::class == this::class && left == other.left && right == other.right
    override fun hashCode(): Int = listOf(javaClass, left, right).hashCode()
}
