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

/**
 * Converts an HSL color value to RGB. Conversion formula
 * adapted from http://en.wikipedia.org/wiki/HSL_color_space.
 * Assumes h, s, and l are contained in the set [0, 1] and
 * returns r, g, and b in the set [0, 255].
 *
 * from: https://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
 *
 * @param h       The hue
 * @param s       The saturation
 * @param l       The lightness
 * @return int array, the RGB representation
 */
fun hslToRgb(h: Float, s: Float, l: Float): IntArray {
    val r: Float
    val g: Float
    val b: Float
    if (s == 0f) {
        b = l
        g = b
        r = g // achromatic
    } else {
        val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
        val p = 2 * l - q
        r = hueToRgb(p, q, h + 1f / 3f)
        g = hueToRgb(p, q, h)
        b = hueToRgb(p, q, h - 1f / 3f)
    }
    return intArrayOf(to255(r), to255(g), to255(b))
}

private fun to255(v: Float): Int {
    return 255f.coerceAtMost(256 * v).toInt()
}

/** Helper method that converts hue to rgb  */
private fun hueToRgb(p: Float, q: Float, t: Float): Float {
    var t = t
    if (t < 0f) t += 1f
    if (t > 1f) t -= 1f
    if (t < 1f / 6f) return p + (q - p) * 6f * t
    if (t < 1f / 2f) return q
    return if (t < 2f / 3f) p + (q - p) * (2f / 3f - t) * 6f else p
}
