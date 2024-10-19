package com.yoloroy.interpreter

data class StringManageable(val value: String) : Manageable() {
    override val metatable = mapOf(
        ManageableFunction.CommonName.Plus.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? StringManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s]
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction StringManageable(a.value + b.toString())
        },
        ManageableFunction.CommonName.Multiply.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? StringManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction StringManageable(a.value.repeat(b.value))
        }
    )

    override fun toString(): String = value
}
