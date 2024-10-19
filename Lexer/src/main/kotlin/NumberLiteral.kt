package com.yoloroy.lexer

import kotlin.Number

internal fun identifyNumbers(lexemes: List<Lexeme>): List<Lexeme> = lexemes
    .flatMap { if (it is Mess) NumberLiteral.parser.parse(it) else listOf(it) }
    .filterNot { it is Mess && it.text.isBlank() }

sealed class NumberLiteral(open val number: Number) : Lexeme {
    companion object {
        private val subtypesParser by lazy { LexemeCombinedParser(listOf(IntLiteral.parser)) }
        internal val parser = object : LexemeParser {
            override fun parse(mess: Mess): List<Lexeme> = mess.text.trim()
                .split("\\s+".toRegex())
                .flatMap { subtypesParser.parse(Mess(it)) }
        }
    }
}

data class IntLiteral(override val number: Int) : NumberLiteral(number) {
    companion object {
        internal val parser = object : LexemeParser {
            override fun parse(mess: Mess): List<Lexeme> = mess.text.toIntOrNull()
                ?.let { listOf(IntLiteral(it)) }
                ?: listOf(mess)
        }
    }
}

// TODO: others
