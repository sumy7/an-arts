package com.sumygg.anarts.arts.cisterciannumerals

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate

/**
 * 每个数字路径(x, y)格式关键点
 */
private val SYMBOL_PATH = arrayOf(
    arrayOf(0, 0),                      // 0
    arrayOf(0, 0, 1, 0),                // 1
    arrayOf(0, 1, 1, 1),                // 2
    arrayOf(0, 0, 1, 1),                // 3
    arrayOf(0, 1, 1, 0),                // 4
    arrayOf(0, 0, 1, 0, 0, 1),          // 5
    arrayOf(1, 0, 1, 1),                // 6
    arrayOf(0, 0, 1, 0, 1, 1),          // 7
    arrayOf(0, 1, 1, 1, 1, 0),          // 8
    arrayOf(0, 0, 1, 0, 1, 1, 0, 1),    // 9
)

/**
 * 将数字转换成数字路径
 * @param n 单个数字，0-9
 * @return 绘制路径
 */
fun digitPath(n: Int): Path {
    val p = SYMBOL_PATH[n % 10]
    val path = Path()
    path.moveTo(p[0].toFloat(), p[1].toFloat())
    for (i in 2 until p.size step 2) {
        path.lineTo(p[i].toFloat(), p[i + 1].toFloat())
    }
    return path
}

/**
 * 在Canvas DrawScope中绘制cistercianNumerals。
 * 大小 2倍size * 3倍size。
 * 绘制时以最终图形的中心为远点绘制，需提前将画布移动到绘制中心。
 *
 * @param x 需要绘制的数字，范围0-9999
 * @param size 绘制的大小
 */
fun DrawScope.cistercianNumeralsSymbol(x: Int, size: Float, color: Color) {

    translate(0f, -1.5f * size) {
        // 基准竖线
        drawLine(color, Offset(0f, 0f), Offset(0f, 3f * size), strokeWidth = 0.3f * size)

        translate(0f, 0f) {
            // 右上，个位
            scale(1f * size, 1f * size, Offset(0f, 0f)) {
                val path1 = digitPath(x % 10)
                drawPath(path1, color, style = Stroke(0.3f))
            }

            // 左上，十位
            scale(-1f * size, 1f * size, Offset(0f, 0f)) {
                val path2 = digitPath(x / 10 % 10)
                drawPath(path2, color, style = Stroke(0.3f))
            }
        }
        translate(0f, 3f * size) {
            // 右下，百位
            scale(1f * size, -1f * size, Offset(0f, 0f)) {
                val path3 = digitPath(x / 100 % 10)
                drawPath(path3, color, style = Stroke(0.3f))
            }

            // 左下，千位
            scale(-1f * size, -1f * size, Offset(0f, 0f)) {
                val path4 = digitPath(x / 1000 % 10)
                drawPath(path4, color, style = Stroke(0.3f))
            }
        }
    }
}
