package com.sumygg.anarts.arts.maze

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig

/**
 * 绘制一个随机的Maze图像
 * @param config 图像配置
 * @return 绘制函数
 */
fun Maze(config: ArtsConfig): DrawScope.() -> Unit {
    if (config !is MazeConfig) {
        throw IllegalArgumentException("Maze must use MazeConfig")
    }

    return {
        val step = 10
        for (x in 0 until this.size.width.toInt() step step) {
            for (y in 0 until this.size.height.toInt() step step) {
                if (Math.random() > 0.5) {
                    drawLine(
                        Color.Green,
                        Offset(x.toFloat(), y.toFloat()),
                        Offset((x + step).toFloat(), (y + step).toFloat())
                    )
                } else {
                    drawLine(
                        Color.Green,
                        Offset((x + step).toFloat(), y.toFloat()),
                        Offset(x.toFloat(), (y + step).toFloat())
                    )
                }
            }
        }
    }
}
