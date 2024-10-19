package com.yoloroy.lexer

internal interface LexemeParser {
    fun parse(mess: Mess): List<Lexeme>
}
