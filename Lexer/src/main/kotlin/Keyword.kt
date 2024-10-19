package com.yoloroy.lexer

internal fun identifyKeywords(origLexemes: List<Lexeme>): List<Lexeme> {
    // could be optimized by replacing this bfs to recursive dfs
    var lexemes = origLexemes
    do {
        var wasChanged = false
        val newList = lexemes.flatMap { if (it is Mess) it.reparse() else listOf(it) }

        if (lexemes.size != newList.size) {
            wasChanged = true
        } else {
            checkElementChanged@ for (i in lexemes.indices) {
                if (lexemes[i] !== newList[i]) {
                    wasChanged = true
                    break@checkElementChanged
                }
            }
        }

        lexemes = newList
    } while (wasChanged)

    return lexemes
}

enum class Keyword(val word: String, val standalone: Boolean) : Lexeme {
    EndLine("\n", false),
    Comment("//", false),

    First(".!", false),
    Count("#", false),
    CastOrNull("of?", true),
    Cast("of", true),
    Assign("<-:", false),
    Map(".->", false),
    MapCatching(".~>", false),
    Quote("\"", false),

    Generator(">>", false),

    SwitchCase("?:", false),
    SwitchCaseCase("|->", false),
    SwitchCaseElse("|~>", false),

    OpenParen("(", false),
    CloseParen(")", false),

    Dot(".", false),

    Equals("==", false),
    NotEquals("!=", false),

    Plus("+", false),
    Minus("-", false),
    Div("/", false),
    Mul("*", false),

    Mod("%", false),

    Colon(":", false);

    companion object {
        internal val parsers = entries.map {
            if (it.standalone) {
                StandaloneKeywordParser(it)
            } else {
                NonStandaloneKeywordParser(it)
            }
        }
    }
}

internal class StandaloneKeywordParser(private val keyword: Keyword) : LexemeParser {
    override fun parse(mess: Mess): List<Lexeme> {
        if (keyword.word !in mess.text) return listOf(mess)

        val (left, right) = mess.text.split(keyword.word, limit = 2)
        val isSpacedOnLeft = left.isEmpty() || left.last().isWhitespace()
        val isSpacedOnRight = right.isEmpty() || right.first().isWhitespace()

        return if (isSpacedOnLeft && isSpacedOnRight) {
            listOf(Mess(left), keyword, Mess(right))
        } else {
            listOf(mess)
        }
    }
}

internal class NonStandaloneKeywordParser(private val keyword: Keyword) : LexemeParser {
    override fun parse(mess: Mess): List<Lexeme> {
        if (keyword.word !in mess.text) return listOf(mess)

        val (left, right) = mess.text.split(keyword.word, limit = 2)
        return listOf(Mess(left), keyword, Mess(right))
    }
}
