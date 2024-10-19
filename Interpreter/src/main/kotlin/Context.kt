package com.yoloroy.interpreter

import com.yoloroy.parser.Block
import com.yoloroy.parser.parseMany

private val reservedVariables = run {
    lateinit var outputLine: SpecialManageable
    outputLine = SpecialManageable(
        onBind = { println(it) },
        onResolve = { outputLine }
    )

    mapOf(
        "input_lines" to StreamManageable(generateSequence { StringManageable(readln()) }.iterator()),
        "output_line" to outputLine,
        "Int" to IntTypeManageable,
        "Nothing" to NothingManageable
    )
}

open class Context {

    protected val variables = mutableMapOf<String, Manageable>()

    open fun bindReservedVariables() {
        variables += reservedVariables
    }

    open fun bind(name: String, value: Manageable) {
        if (this@Context.tryBindSpecial(name, value)) {
            return
        }
        if (name in reservedVariables) {
            throw ReservedValueCannotBeChangedException(name)
        }
        variables[name] = value
    }

    protected open fun tryBindSpecial(name: String, value: Manageable): Boolean {
        return when (val m = variables[name]) {
            is SpecialManageable -> {
                m.onBind(value)
                true
            }
            else -> false
        }
    }

    open fun resolve(name: String): Manageable {
        return variables[name] ?: throw ContextException("$name is not defined in this context")
    }

    inner class Local(private val additionalValues: Map<String, Manageable> = emptyMap()) : Context() {

        override fun bind(name: String, value: Manageable) {
            if (name in additionalValues) {
                throw ContextException("$name cannot be changed")
            }
            try {
                this@Context.bind(name, value)
                return
            } catch (_: ReservedValueCannotBeChangedException) {
            }

            super.bind(name, value)
        }

        override fun resolve(name: String): Manageable =
            variables[name] ?:
            additionalValues[name] ?:
            this@Context.resolve(name)
    }

    class ReservedValueCannotBeChangedException(nameToChange: String) : ContextException("`$nameToChange` is reserved")
    open class ContextException(message: String? = null, cause: Exception? = null) : IllegalArgumentException(message, cause)
}
