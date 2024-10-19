package com.yoloroy.interpreter

import com.yoloroy.parser.*

fun runExpression(expression: Expression, context: Context): Manageable {
    return try {
        when (expression) {
            is Identifier -> context.resolve(expression.name)
            is Value<*> -> Manageable.of(expression.value)
            is Assignment -> runExpression(expression.expression, context).also {
                context.bind(
                    expression.assignee.name,
                    it
                )
            }

            is BinaryOperator -> runBinaryOperator(expression, context)
            is FirstOperator -> StreamManageable.first(expression.stream, context)
            is CountOperator -> StreamManageable.count(expression.stream, context)
            is CollectionOperation -> (runExpression(expression.source, context) as StreamManageable).map(expression.mapping, context)

            is Block -> runBlock(context, expression)
            is SwitchCase -> runSwitchCase(expression.selector, expression.cases, context)
            is Generator -> StreamManageable.ofGenerator(expression, context)

            else -> throw InterpreterException("This expression tree could not be produced by actual parser")
        }
    } catch (e: Exception) {
        throw InterpreterException(e.toString())
    }
}

private fun runBlock(
    context: Context,
    expression: Block
) = try {
    val localContext = context.Local()
    expression.lines.asSequence().mapIndexed { index, line ->
        try {
            runExpression(line, localContext)
        } catch (e: InterpreterException) {
            throw InterpreterException(e.toString() + " in ${index}th line")
        }
    }.last()
} catch (e: InterpreterException) {
    throw InterpreterException(e.toString(), expression)
}

private fun runBinaryOperator(operator: BinaryOperator, context: Context): Manageable {
    val functionName = ManageableFunction.CommonName.by(operator)
    val args = mapOf(ManageableFunction.CommonArg.Other.s to runExpression(operator.right, context))
    val self = runExpression(operator.left, context)

    return self.callMethod(functionName, context, args)
}

private fun runSwitchCase(selector: Expression, cases: List<SwitchCase.Case>, context: Context): Manageable {
    val selected = runExpression(selector, context)

    return cases.firstOrNull {
        when (it) {
            is SwitchCase.ParametrizedCase -> selected == runExpression(it.case, context)
            is SwitchCase.ElseCase -> true
        }
    }?.run {
        runExpression(block, context.Local())
    } ?: NothingManageable
}
