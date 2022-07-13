package com.sumygg.anarts.utils

/**
 * 将byte数组转换成16进制字符串
 */
fun ByteArray.toHexString(): String {
    val sb = StringBuilder()
    for (b in this) {
        sb.append(String.format("%02x", b))
    }
    return sb.toString()
}
