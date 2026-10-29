package com.sumygg.anarts.utils

import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

// Read all fields from one Date instance so a second/day rollover cannot mix values.
@OptIn(ExperimentalWasmJsInterop::class)
private fun localDateTimeFields(): String = js(
    "{ const d = new Date(); return [d.getFullYear(), d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes(), d.getSeconds()].join(','); }"
)

actual fun localDateTimeNow(): LocalDateTimeParts {
    val fields = localDateTimeFields().split(',').map(String::toInt)
    return LocalDateTimeParts(fields[0], fields[1], fields[2], fields[3], fields[4], fields[5])
}
