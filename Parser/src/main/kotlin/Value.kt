package com.yoloroy.parser

import com.yoloroy.lexer.Identifier as LexerIdentifier
import com.yoloroy.lexer.Lexeme
import com.yoloroy.lexer.NumberLiteral
import com.yoloroy.lexer.TextLiteral

internal fun Value.Companion.from(lexeme: Lexeme): Expression {
    return when (lexeme) {
        is LexerIdentifier -> Identifier(lexeme.name)
        is NumberLiteral -> Value(lexeme.number)
        is TextLiteral -> Value(lexeme.text)
        else -> throw UnknownError("This cannot happen")
    }
}

/**
 * TODO `type: Type`
 */
data class Value<T>(val value: T): Expression {
    companion object
}
