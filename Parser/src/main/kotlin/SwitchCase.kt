package com.yoloroy.parser

import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.Lexeme
import com.yoloroy.parser.util.subList

fun SwitchCase.Companion.from(lexemes: List<Lexeme>): Expression {
    val operationI = lexemes.indexOfFirst { it == Keyword.SwitchCase }
    val selector = Expression.from(lexemes.subList(0 ..< operationI))
    val cases = lexemes[operationI + 1]
        .let {
            if (it is Block) {
                it.lines
            } else {
                lexemes.subList(operationI + 1 .. lexemes.lastIndex)
            }
        }
        .let(::parseCases)

    return SwitchCase(selector, cases)
}

fun SwitchCase.ParametrizedCase.Companion.from(lexemes: List<Lexeme>): SwitchCase.Case {
    val operationI = lexemes.indexOfFirst { it == Keyword.SwitchCaseCase }
    val case = Expression.from(lexemes.subList(0 ..< operationI))
    val block = Expression.from(lexemes.subList(operationI + 1 .. lexemes.lastIndex))

    return SwitchCase.ParametrizedCase(case, block)
}

fun SwitchCase.ElseCase.Companion.from(lexemes: List<Lexeme>): SwitchCase.Case {
    val block = Expression.from(lexemes.subList(1 .. lexemes.lastIndex))

    return SwitchCase.ElseCase(block)
}

private fun parseCases(lexemes: List<Lexeme>): List<SwitchCase.Case> {
    return lexemes.map { it as SwitchCase.Case }
}

data class SwitchCase(val selector: Expression, val cases: List<Case>) : Expression {
    @Suppress("RemoveEmptyClassBody") // it is not redundant
    companion object {}

    sealed interface Case : Expression {
        val block: Expression
    }

    data class ParametrizedCase(val case: Expression, override val block: Expression) : Case {
        companion object
    }

    data class ElseCase(override val block: Expression) : Case {
        companion object
    }
}
