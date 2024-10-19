package com.yoloroy.interpreter

sealed class Manageable {

    protected abstract val metatable: Map<String, ManageableFunction>

    companion object {
        fun of(value: Any?) = when (value) {
            null -> NothingManageable
            is Int -> IntManageable(value)
            is String -> StringManageable(value)
            else -> TODO()
        }
    }

    fun call(functionName: String, context: Context, arguments: Map<String, Manageable>): Manageable {
        return metatable[functionName]?.run(context, arguments)
            ?: throw NoSuchFunctionException(functionName)
    }

    fun callMethod(functionName: String, context: Context, arguments: Map<String, Manageable>): Manageable {
        return call(functionName, context, arguments + (ManageableFunction.CommonArg.This.s to this))
    }

    inner class NoSuchFunctionException(name: String) : InterpreterException("Function $name does not exists in metatable of class ${this@Manageable::class}")
    inner class IllegalArgumentTypeException(name: String, shouldBe: String) : InterpreterException("Illegal argument type, $name should be $shouldBe")
}
