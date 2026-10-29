package com.sumygg.anarts.utils

/** Device-local calendar fields used by the Cistercian clock. */
data class LocalDateTimeParts(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int,
    val second: Int
)

expect fun localDateTimeNow(): LocalDateTimeParts
