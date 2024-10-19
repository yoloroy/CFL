package com.yoloroy.interpreter

import com.yoloroy.parser.BinaryOperator
import com.yoloroy.parser.Expression
import kotlin.reflect.KClass

abstract class ManageableFunction : Manageable() {

    abstract fun run(context: Context, arguments: Map<String, Manageable>): Manageable

    override val metatable get() = mapOf(CommonName.Call.s to this)

    companion object {
        operator fun invoke(block: (context: Context, arguments: Map<String, Manageable>) -> Manageable): ManageableFunction = Stored(block)
    }

    class Declared(
        private val expression: Expression,
        private val declarationContext: Context
    ) : ManageableFunction() {
        override fun run(context: Context, arguments: Map<String, Manageable>): Manageable {
            return runExpression(expression, declarationContext.Local(arguments))
        }
    }

    private class Stored(private val stored: (context: Context, arguments: Map<String, Manageable>) -> Manageable) : ManageableFunction() {
        override fun run(context: Context, arguments: Map<String, Manageable>): Manageable = stored(context, arguments)
    }

    enum class CommonName(val s: String, private val clazz: KClass<*>) {
        Plus("__plus", BinaryOperator.Plus::class),
        Minus("__minus", BinaryOperator.Minus::class),
        Multiply("__multiply", BinaryOperator.Multiply::class),
        Div("__divide", BinaryOperator.Divide::class),
        Cast("__cast", BinaryOperator.Cast::class),
        CastOrNull("__castOrNull", BinaryOperator.CastOrNull::class),
        Mod("__mod", BinaryOperator.Mod::class),
        Equals("__equals", BinaryOperator.Equals::class),
        NotEquals("__notEquals", BinaryOperator.NotEquals::class),
        Call("__call", ManageableFunction::class);

        companion object {
            fun by(operator: BinaryOperator) = entries
                .firstOrNull { it.clazz == operator::class }?.s
                ?: throw ThereIsNoCommonNameForThisFunction(operator)
        }

        class ThereIsNoCommonNameForThisFunction(function: Any) : InterpreterException("There is no common name for this function: $function")
    }

    enum class CommonArg(val s: String) {
        This("this"),
        Other("other"),
        Each("each")
    }

    class BadArgumentException(argumentName: String) : InterpreterException("Bad argument: $argumentName")
}
