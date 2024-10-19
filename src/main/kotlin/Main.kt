package com.yoloroy

import com.yoloroy.interpreter.Context
import com.yoloroy.interpreter.InterpreterException
import com.yoloroy.interpreter.runExpression
import com.yoloroy.lexer.LexerException
import com.yoloroy.lexer.identifyLexemes
import com.yoloroy.parser.Block
import com.yoloroy.parser.ExpressionParsingException
import com.yoloroy.parser.parseMany

fun main() {
    val multilineCode = """
        n <-: 10
        #(>>: n != 0 . .-> :
            output_line <-: " " * n + "*" * n
            n <-: n - 1
        )
    """.trimIndent()
    /*val multilineCode = """
        output_line <-: "Вводите числа для суммирования, не число прервёт выполнение:"
        sum <-: 0
        summing <-: input_lines .-> Int of? each .-> :
            sum <-: sum + each
            output_line <-: "Сумма: " + sum
            output_line <-: "Введите следующее число:"
        .
        #summing
    """.trimIndent()*/
    /*val multilineCode = "output_line <-: (10 + 10 / 3) * 4"*/
    /*val multilineCode = """
        output_line <-: "Введите последнее число для обработки fizz-buzz'а" //"comment here"
        end <-: (input_lines .-> Int of? each) .! //"comment there"
        end ?: :
            Nothing |-> :
                output_line <-: "Вы ввели не число, держите 1:"
                end <-: 1
            .
        .

        i <-: 1

        results <-: >>: i != (end + 1). .-> :
            0 ?: :
                each % 15 |-> output_line <-: "FizzBuzz"
                each % 3 |-> output_line <-: "Fizz"
                each % 5 |-> :
                    output_line <-: "Buzz"
                .
                |~> :
                    output_line <-: each
                .
            .
            i <-: i + 1
        .
        #results
    """.trimIndent()*/

    println("Interpreting code:\n```\n$multilineCode\n```")

    val lexemes = try {
        identifyLexemes(multilineCode)
    } catch (e: LexerException) {
        printlnErr("Bad code, even lexemes cannot be parsed: $e")
        return
    }

    val blocks = try {
        Block.parseMany(lexemes)
    } catch (e: ExpressionParsingException) {
        printlnErr("Bad code, expressions are bad: $e")
        return
    }

    val context = Context().apply {
        bindReservedVariables()
    }
    for ((index, block) in blocks.withIndex()) try {
        runExpression(block, context)
    } catch (e: InterpreterException) {
        printlnErr("exception in ${index}th block($block) during interpretation:\n$e")
        return
    }
}

private fun printlnErr(any: Any) = System.err.println(any)
