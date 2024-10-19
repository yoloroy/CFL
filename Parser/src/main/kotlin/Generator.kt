package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun Generator.Companion.from(lexemes: List<Lexeme>): Expression {
    val openI = lexemes.indexOfFirst { it == Keyword.Generator }

    return Expression.from(
        lexemes.subList(0 ..< openI) +
        Generator(Expression.from(listOf(lexemes[openI + 1]))) +
        lexemes.subList(openI + 2 .. lexemes.lastIndex)
    )
}

data class Generator(val expression: Expression) : Expression {
    companion object
}
