package com.yoloroy.lexer

internal fun identifyStrings(text: String): List<Lexeme> = text
    .splitToSequence('"')
    .mapIndexed { i, it -> if (i % 2 == 0) Mess(it) else TextLiteral(it) }
    .filterNot { it is Mess && it.text.isBlank() }
    .toList()

data class TextLiteral(val text: String) : Lexeme
