package com.yoloroy.interpreter

import com.yoloroy.parser.Expression
import com.yoloroy.parser.Generator

open class StreamManageable(private val iterator: Iterator<Manageable>) : Manageable() {

    override val metatable = mapOf<String, ManageableFunction>()

    companion object {
        fun ofGenerator(generator: Generator, context: Context): StreamManageable {
            val sequence = generateSequence {
                runExpression(generator.expression, context).takeIf { it != NothingManageable }
            }
            return StreamManageable(sequence.iterator())
        }

        fun first(expression: Expression, context: Context): Manageable {
            val stream = runExpression(expression, context) as? StreamManageable ?: throw NotAStreamException()
            return stream.iterator.next()
        }

        fun count(expression: Expression, context: Context): IntManageable {
            val stream = runExpression(expression, context) as? StreamManageable ?: throw NotAStreamException()
            return IntManageable(stream.iterator.asSequence().count())
        }
    }

    fun map(transform: Expression, context: Context): StreamManageable {
        val mapped = iterator
            .asSequence()
            .takeWhile { it !is NothingManageable }
            .map { runExpression(transform, context.Local(mapOf(ManageableFunction.CommonArg.Each.s to it))) }
            .iterator()

        return StreamManageable(mapped)
    }

    class NotAStreamException : Exception()
}
