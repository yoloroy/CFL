package com.yoloroy.lexer

fun identifyLexemes(text: String): List<Lexeme> {
    return try {
        identifyNumbers(identifyKeywords(identifyStrings(text)))
    } catch (e: Exception) {
        throw LexerException(e)
    }
}

interface Lexeme

internal class LexemeCombinedParser(private val subcategories: List<LexemeParser>) : LexemeParser {
    override fun parse(mess: Mess): List<Lexeme> {
        if (mess.text.isBlank()) {
            return emptyList()
        }

        1 + 2;

        return subcategories
            .asSequence()
            .map { it.parse(mess) }
            .firstOrNull { it[0] !== mess }
            ?: listOf(mess)
    }
}

class LexerException(cause: Throwable?) : Throwable(cause)
