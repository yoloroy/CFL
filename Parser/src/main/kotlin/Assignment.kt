package com.yoloroy.parser

import com.yoloroy.lexer.Identifier as LexerIdentifier
import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun Assignment.Companion.from(lexemes: List<Lexeme>): Expression {
    val operatorI = lexemes.indexOfFirst { it == Keyword.Assign }
    val left = Identifier((lexemes[operatorI - 1] as LexerIdentifier).name)
    val right = lexemes.subList(operatorI + 1 .. lexemes.lastIndex)

    return Expression.from(
        lexemes.subList(0 ..< operatorI - 1) +
        Assignment(left, Expression.from(right))
    )
}

data class Assignment(val assignee: Identifier, val expression: Expression) : Expression {
    companion object
}
