import com.yoloroy.lexer.IntLiteral
import com.yoloroy.lexer.Keyword
import com.yoloroy.lexer.TextLiteral
import com.yoloroy.parser.*
import com.yoloroy.lexer.Identifier as IdentifierLexeme
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ExpressionKtTest {

    @Test
    fun `test parseExpression`() {
        // Test simple addition
        val lexemes = listOf(IntLiteral(1), Keyword.Plus, IntLiteral(2))
        val expected = BinaryOperator(Keyword.Plus, Value(1), Value(2))
        assertEquals(expected, Expression.from(lexemes))

        // Test subtraction
        val lexemesSubtraction = listOf(IntLiteral(3), Keyword.Minus, IntLiteral(2))
        val expectedSubtraction = BinaryOperator(Keyword.Minus, Value(3), Value(2))
        assertEquals(expectedSubtraction, Expression.from(lexemesSubtraction))

        // Test multiplication
        val lexemesMultiplication = listOf(IntLiteral(4), Keyword.Mul, IntLiteral(2))
        val expectedMultiplication = BinaryOperator(Keyword.Mul, Value(4), Value(2))
        assertEquals(expectedMultiplication, Expression.from(lexemesMultiplication))

        // Test division
        val lexemesDivision = listOf(IntLiteral(8), Keyword.Div, IntLiteral(2))
        val expectedDivision = BinaryOperator(Keyword.Div, Value(8), Value(2))
        assertEquals(expectedDivision, Expression.from(lexemesDivision))
    }

    @Test
    fun `test parseParenthesis`() {
        // Test simple expression within parentheses
        val lexemes = listOf(Keyword.OpenParen, IntLiteral(1), Keyword.Plus, IntLiteral(2), Keyword.CloseParen)
        val expected = BinaryOperator(Keyword.Plus, Value(1), Value(2))
        assertEquals(expected, Expression.from(lexemes))
    }

    @Test
    fun `test parseBinaryOperator`() {
    }

    @Test
    fun `test parseValue`() {
        assertEquals(Value(0), Value.from(IntLiteral(0)))
        assertEquals(Value("hmm"), Value.from(TextLiteral("hmm")))
        assertEquals(Identifier("hmm"), Value.from(IdentifierLexeme("hmm")))
        assertThrows<UnknownError> { Value.from(Keyword.Plus) }
    }
}
