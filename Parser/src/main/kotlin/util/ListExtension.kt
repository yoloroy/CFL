package com.yoloroy.parser.util

internal fun <T> List<T>.subList(range: IntRange) = subList(range.first, range.last + 1)
