package com.sumygg.anarts.utils

import kotlin.math.pow

/**
 * easeOutQuart 缓动函数
 * @param x 表示 0（动画开始）到 1（动画结束）范围内的值
 */
fun easeOutQuart(x: Double): Double {
    return 1.0 - (1 - x).pow(4.0)
}
