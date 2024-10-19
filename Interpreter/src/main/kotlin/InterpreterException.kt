package com.yoloroy.interpreter

import com.yoloroy.parser.Block

open class InterpreterException(message: String, block: Block? = null) : Throwable("$message in ${block?.let { "block($it)" } ?: "script"}")
