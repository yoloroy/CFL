package com.yoloroy.interpreter

data class IntManageable(val value: Int) : Manageable() {

    override val metatable = mapOf(
        ManageableFunction.CommonName.Plus.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction IntManageable(a.value + b.value)
        },
        ManageableFunction.CommonName.Minus.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction IntManageable(a.value - b.value)
        },
        ManageableFunction.CommonName.Multiply.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction IntManageable(a.value * b.value)
        },
        ManageableFunction.CommonName.Div.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction IntManageable(a.value / b.value)
        },
        ManageableFunction.CommonName.Mod.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction IntManageable(a.value % b.value)
        },
        ManageableFunction.CommonName.Equals.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction if (a.value == b.value) IntManageable(a.value) else NothingManageable
        },
        ManageableFunction.CommonName.NotEquals.s to ManageableFunction { context, arguments ->
            assert(arguments.size == 2)

            val a = arguments[ManageableFunction.CommonArg.This.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.This.s,
                    IntManageable::class.java.name
                )
            val b = arguments[ManageableFunction.CommonArg.Other.s] as? IntManageable
                ?: throw IllegalArgumentTypeException(
                    ManageableFunction.CommonArg.Other.s,
                    IntManageable::class.java.name
                )

            return@ManageableFunction if (a.value != b.value) IntManageable(a.value) else NothingManageable
        }
    )

    override fun toString(): String = value.toString()
}
