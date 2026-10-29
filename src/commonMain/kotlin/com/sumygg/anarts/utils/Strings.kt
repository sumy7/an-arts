package com.sumygg.anarts.utils

/** 将byte数组转换成小写16进制字符串。 */
fun ByteArray.toHexString(): String = joinToString("") {
    (it.toInt() and 0xff).toString(16).padStart(2, '0')
}
