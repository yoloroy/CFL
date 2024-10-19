package com.yoloroy.lexer

data class Mess(val text: String) : Lexeme {
    companion object {
        internal val parser = LexemeCombinedParser(Keyword.parsers + Identifier.parser)
    }

    internal fun reparse(): List<Lexeme> = parser.parse(this)
}
