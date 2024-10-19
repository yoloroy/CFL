package util

import com.yoloroy.parser.util.ListRegex
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ListRegexTest {

    @Test
    fun firstMatchOrNull() {
        assertEquals(
            0..2,
            ListRegex<Int>(
                { _, start -> start },
                { _, start -> start },
                { _, start -> start }
            ).firstMatchOrNull(listOf(0, 1, 2))
        )
        assertEquals(
            2..2,
            ListRegex<Int>({ list, i -> i.takeIf { list[i] > 0 } }).firstMatchOrNull(listOf(-1, 0, 1, 2, 3))
        )
        assertEquals(
            2..4,
            ListRegex<Int>(
                { list, i -> i.takeIf { list[i] > 0 } }, // number > 0 met once
                { list, i -> list.subList(i, list.size).indexOfLast { it > 0 } + i }, // number > 0 met once
            ).firstMatchOrNull(listOf(-1, 0, 1, 2, 3, -1, 0))
        )
    }
}
