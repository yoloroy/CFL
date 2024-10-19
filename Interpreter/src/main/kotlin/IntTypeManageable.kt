package com.yoloroy.interpreter

data object IntTypeManageable : Manageable() {
    override val metatable = mapOf(
        ManageableFunction.CommonName.CastOrNull.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 1)

            val int = when (val a = arguments.values.first()) {
                is IntManageable -> a.value
                is StringManageable -> a.value.toIntOrNull()
                else -> throw TypeCastException()
            }

            return@ManageableFunction int?.let(::IntManageable) ?: NothingManageable
        },
        ManageableFunction.CommonName.Cast.s to ManageableFunction { _, arguments ->
            assert(arguments.size == 1)

            val int = when (val a = arguments.values.first()) {
                is IntManageable -> a.value
                is StringManageable -> a.value.toInt()
                else -> throw TypeCastException()
            }

            return@ManageableFunction IntManageable(int)
        }
    )
}
