package com.sumygg.anarts.arts.maze

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

/**
 * Maze Arts
 */
class Maze : BaseArts("Maze") {
    override fun onInit() {
        // 无需要初始化
    }

    override fun onUpdate(elapsedNanos: Long) {
        // 无需要更新内部变量
    }

    /**
     * 绘制一个随机的Maze图像
     * @param config 图像配置
     * @return 绘制函数
     */
    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is MazeConfig) {
            throw IllegalArgumentException("${this.name} must use ${MazeConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            val step = config.step.value
            for (x in 0 until this.size.width.toInt() step step) {
                for (y in 0 until this.size.height.toInt() step step) {
                    if (Math.random() > 0.5) {
                        drawLine(
                            config.foreground.value,
                            Offset(x.toFloat(), y.toFloat()),
                            Offset((x + step).toFloat(), (y + step).toFloat()),
                            config.lineWidth.value
                        )
                    } else {
                        drawLine(
                            config.foreground.value,
                            Offset((x + step).toFloat(), y.toFloat()),
                            Offset(x.toFloat(), (y + step).toFloat()),
                            config.lineWidth.value
                        )
                    }
                }
            }
        }
    }
}
