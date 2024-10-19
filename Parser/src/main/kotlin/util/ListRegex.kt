package com.yoloroy.parser.util

class ListRegex<T>(private vararg val parts: Predicate<T>) {

    init {
        if (parts.isEmpty()) {
            throw IllegalStateException("parts should not be empty")
        }
    }

    fun firstMatchOrNull(list: List<T>): IntRange? {
        var partI = 0
        var globalStartI = 0
        var endI: Int?

        do {
            endI = parts[partI].findEnd(list, globalStartI)
        } while (endI == null && ++globalStartI in list.indices)

        if (endI == null) {
            return null
        }
        if (parts.size == 1) {
            return globalStartI..endI
        }

        partI++
        var startI: Int = endI + 1
        do {
            endI = parts[partI++].findEnd(list, startI) ?: return null
            startI = endI + 1
        } while (partI in parts.indices && startI in list.indices)

        return globalStartI..endI!!
    }

    fun interface Predicate<T> {
        fun findEnd(list: List<T>, start: Int): Int?
    }
}
