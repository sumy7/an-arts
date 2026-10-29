package com.sumygg.anarts.utils

import java.time.LocalDateTime

actual fun localDateTimeNow(): LocalDateTimeParts {
    val now = LocalDateTime.now()
    return LocalDateTimeParts(now.year, now.monthValue, now.dayOfMonth, now.hour, now.minute, now.second)
}
