package com.sumygg.anarts.utils

/**
 * 将androidx中的Color对象转换成swing的Color对象。
 */
fun androidx.compose.ui.graphics.Color.toSwingColor(): java.awt.Color {
    return java.awt.Color(this.red, this.green, this.blue, this.alpha)
}

/**
 * 将swing的Color对象转换成androidx的Color对象。
 */
fun java.awt.Color.toAndroidxColor(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(this.red, this.green, this.blue, this.alpha)
}

/**
 * 将androidx的Color对象转换成hex字符串，格式RGBA。
 */
fun androidx.compose.ui.graphics.Color.toHexString(): String {
    return String.format(
        "#%02X%02X%02X%02X",
        (this.red * 255.0f + 0.5f).toInt(),
        (this.green * 255.0f + 0.5f).toInt(),
        (this.blue * 255.0f + 0.5f).toInt(),
        (this.alpha * 255.0f + 0.5f).toInt()
    )
}

/**
 * 对androidx颜色取反，与原颜色对比突出显示。会舍弃原颜色的透明度属性。
 */
fun androidx.compose.ui.graphics.Color.reverse(): androidx.compose.ui.graphics.Color {
    return androidx.compose.ui.graphics.Color(
        1.0f - this.red,
        1.0f - this.green,
        1.0f - this.blue
    )
}
