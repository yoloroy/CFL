package com.yoloroy.lexer

data class Identifier(val name: String) : Lexeme {
    internal companion object {
        internal val parser = object : LexemeParser {
            override fun parse(mess: Mess): List<Lexeme> {
                return mess.text.trim().split("\\s+".toRegex()).map { word ->
                    if (
                        word.first().isJavaIdentifierStart() &&
                        word.drop(1).all { it.isJavaIdentifierPart() }
                    ) {
                        Identifier(word)
                    } else {
                        mess
                    }
                }
            }
        }
    }
}
