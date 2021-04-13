package com.sumygg.anarts.utils

import kotlin.math.sqrt

/**
 * 计算两个坐标点之间的距离
 * @param x1 坐标点1-x坐标
 * @param y1 坐标点1-y坐标
 * @param x2 坐标点2-x坐标
 * @param y2 坐标点2-y坐标
 * @return 两个点的距离
 */
fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    return sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2))
}
