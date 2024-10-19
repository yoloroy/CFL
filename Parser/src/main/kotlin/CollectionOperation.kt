package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun CollectionOperation.Companion.from(lexemes: List<Lexeme>): CollectionOperation {
    val mapOperationI = lexemes.indexOfLast { it == Keyword.Map }

    return try {
        CollectionOperation(
            Expression.from(lexemes.subList(0..<mapOperationI)),
            Expression.from(lexemes.subList(mapOperationI + 1..lexemes.lastIndex))
        )
    } catch (e: IndexOutOfBoundsException) {
        throw ExpressionParsingException(e.message, e.cause)
    }
}

data class CollectionOperation(val source: Expression, val mapping: Expression) : Expression {
    companion object
}
