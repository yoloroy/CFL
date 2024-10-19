import com.yoloroy.interpreter.Context
import com.yoloroy.interpreter.IntManageable
import com.yoloroy.interpreter.runExpression
import com.yoloroy.parser.BinaryOperator
import com.yoloroy.parser.Value
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class RunExpressionKtTest {

    @Test
    fun `test runExpression`() {
        assertEquals(
            IntManageable(10),
            runExpression(
                BinaryOperator.Plus(
                    Value(4),
                    Value(6)
                ),
                Context()
            ) as? IntManageable
        )
    }
}