package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun Block.Companion.parseMany(multilineLexemes: List<Lexeme>): List<Expression> {
    return parseBlocks(multilineLexemes)
}

private fun parseBlocks(lexemes: List<Lexeme>): List<Expression> {
    var current = lexemes
    while (Keyword.Colon in current) {
        val closing = current.indexOf(Keyword.Dot)
        if (closing == -1) {
            throw ExpressionParsingException("Block was not closed")
        }

        val opening = current.subList(0..<closing).lastIndexOf(Keyword.Colon)

        val beforeBlock = current.subList(0..<opening)
        val afterBlock = current.subList(closing + 1..current.lastIndex)
        val blockInsides = current.subList(opening + 1..<closing)
            .splitByLineEndings()
            .filterNot { it.isEmpty() }
            .map { Expression.from(it) }

        current = beforeBlock + Block(blockInsides) + afterBlock
    }

    return current
        .splitByLineEndings()
        .filterNot { it.isEmpty() }
        .map { Expression.from(it) }
}

private fun List<Lexeme>.splitByLineEndings(): List<List<Lexeme>> {
    if (all { it == Keyword.EndLine }) {
        return emptyList()
    }

    val end = indexOfFirst { it == Keyword.EndLine }
    if (end == lastIndex) {
        return listOf(subList(0 ..< lastIndex))
    }
    if (end == -1) {
        return listOf(this)
    }

    return listOf(subList(0 ..< end)) + subList(end + 1 .. lastIndex).splitByLineEndings()
}

data class Block(val lines: List<Expression>) : Expression {
    companion object
}
