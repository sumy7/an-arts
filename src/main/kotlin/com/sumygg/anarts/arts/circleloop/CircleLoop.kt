package com.sumygg.anarts.arts.circleloop

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.math.cos
import kotlin.math.sin

/**
 * CircleLoop Arts
 */
class CircleLoop : BaseArts("CircleLoop") {

    override fun onInit() {
        // 无需要初始化内部变量
    }

    override fun onUpdate(elapsedNanos: Long) {
        // 静态Arts不需要更新
    }

    /**
     * 绘制CircleLoop
     *
     * @param config circleLoop绘制所需要的外部参数
     * @return 绘制函数
     */
    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is CircleLoopConfig) {
            throw IllegalArgumentException("${this.name} must use ${CircleLoopConfig::class.simpleName}")
        }

        return {
            var theta = 0.0
            val radius = config.radius.value

            drawRect(config.background.value)

            var r = config.radius.value.toDouble()
            for (i in 0..config.iterations.value) {
                translate(
                    this.size.width / 2 - r.toFloat() / 2,
                    this.size.height / 2 - r.toFloat() / 2
                ) {
                    val x = radius * cos(Math.toRadians(theta))
                    val y = radius * sin(Math.toRadians(theta * 2))

                    drawOval(
                        color = config.foreground.value,
                        topLeft = Offset(x.toFloat(), y.toFloat()),
                        size = Size(r.toFloat(), r.toFloat()),
                        style = Stroke(width = config.lineWidth.value)
                    )
                }
                r += cos(theta) * sin(theta / 2) + sin(theta) * cos(theta / 2)
                theta += Math.PI / 2
            }
        }
    }
}
