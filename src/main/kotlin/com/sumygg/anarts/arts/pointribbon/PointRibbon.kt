package com.sumygg.anarts.arts.pointribbon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.math.cos
import kotlin.math.sin

/**
 * 绘制 PointRibbon
 */
class PointRibbon : BaseArts("PointRibbon") {
    override fun onInit() {

    }

    override fun onUpdate(elapsedNanos: Long) {

    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is PointRibbonConfig) {
            throw IllegalArgumentException("${this.name} must use ${PointRibbonConfig::class.simpleName}")
        }

        return {
            drawRect(Color(0xFFE6E6FA))

            var t = 0.0
            var dt = 0.0001
            for (i in 0 until 150000) {
                val delta = 2.0 * config.r.value * cos(4.0 * dt * t) + config.r.value * cos(1.0 * t)
                val blue = 2.0 * config.r.value * sin(t) - config.r.value * cos(3.0 * dt * t)
                val color = Color(delta.toInt(), blue.toInt(), 100, 10)

                val x =
                    2.0 * config.r.value * sin(2.0 * dt * t) + config.r.value * cos(1.0 * t * dt) + this.size.width / 2
                val y = 2.0 * config.r.value * sin(1.0 * dt * t) - config.r.value * sin(5.0 * t) + this.size.height / 2
                drawCircle(color, 1.0f, Offset(x.toFloat(), y.toFloat()))

                t += 0.01
                dt += 0.1
            }
        }
    }
}