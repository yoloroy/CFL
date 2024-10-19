import com.yoloroy.lexer.*
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class LexemeKtTest {

    @Test
    fun `test retrieveExpressionLexemes empty`() {
        assertContentEquals(emptyList(), identifyKeywords(listOf(Mess(""))))
    }

    @Test
    fun `test retrieveExpressionLexemes invalid_identifier`() {
        val mess = identifyKeywords(listOf(Mess("1")))
        assertContentEquals(listOf(Mess("1")), mess)
    }

    @Test
    fun `test retrieveExpressionLexemes non_standalone_standing_together`() {
        assertContentEquals(listOf(Keyword.Quote, Keyword.Quote), identifyKeywords(listOf(Mess("\"\""))))
    }

    @Test
    fun `test identifyStrings`() {
        run { // empty
            val r = identifyStrings("\"\"")
            assertIs<TextLiteral>(r[0])
            assertEquals("", (r[0] as TextLiteral).text)
        }
        run { // with something around and inside
            val r = identifyStrings("a\"bb\"ccc")
            assertIs<Mess>(r[0])
            assertEquals("a", (r[0] as Mess ).text)
            assertIs<TextLiteral>(r[1])
            assertEquals("bb", (r[1] as TextLiteral).text)
            assertIs<Mess>(r[2])
            assertEquals("ccc", (r[2] as Mess ).text)
        }
    }

    @Test
    fun `test identifyNumbers`() {
        assertContentEquals(listOf(IntLiteral(1)), identifyNumbers(listOf(Mess("1"))))
        assertContentEquals(listOf(IntLiteral(1234)), identifyNumbers(listOf(Mess("1234"))))
        assertContentEquals(listOf(IntLiteral(12), IntLiteral(34)), identifyNumbers(listOf(Mess("12 34"))))
    }

    @Test
    fun `test identifyLexemes`() {
        assertContentEquals(
            listOf(IntLiteral(1), Keyword.Plus, IntLiteral(2)),
            identifyLexemes("1 + 2")
        )
        assertContentEquals(
            listOf(),
            identifyLexemes("")
        )
        assertContentEquals(
            listOf(
                Identifier("number1"),
                Keyword.Assign,
                Keyword.OpenParen,
                Identifier("single"),
                Identifier("input_lines"),
                Keyword.Map,
                Identifier("Float"),
                Keyword.Dot,
                Identifier("parsed"),
                Keyword.CloseParen,
                Keyword.Plus,
                IntLiteral(0),
                Keyword.EndLine
            ),
            identifyLexemes("number1 <-: (single input_lines .-> Float.parsed) + 0\n")
        )
        assertContentEquals(
            listOf(
                Identifier("output_line"),
                Keyword.Assign,
                TextLiteral("Summator, enter not number to stop, enter number to add it to sum")
            ),
            identifyLexemes("output_line <-: \"Summator, enter not number to stop, enter number to add it to sum\"")
        )
    }
}
