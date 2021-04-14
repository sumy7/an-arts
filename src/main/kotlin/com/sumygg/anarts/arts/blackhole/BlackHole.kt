package com.sumygg.anarts.arts.blackhole

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.Perlin
import com.sumygg.anarts.utils.random
import kotlin.math.*

/**
 * 绘制 BlackHole
 */
class BlackHole : BaseArts("BlackHole") {
    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is BlackHoleConfig) {
            throw IllegalArgumentException("${this.name} must use ${BlackHoleConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            val kMax = (0.5f..1f).random()

            for (i in 0 until config.circleN.value) {
                val radius = (this.size.width / 10) + i * 0.05f
                val k = kMax * sqrt(1.0 * i / config.circleN.value).toFloat()
                val noisiness = config.density.value * (1.0f * i / config.circleN.value).pow(2)

                var theta = 0f
                var bx = 0f
                var by = 0f
                while (theta < 361f) {
                    val r1 = cos(Math.toRadians(theta.toDouble())).toFloat()
                    val r2 = sin(Math.toRadians(theta.toDouble())).toFloat()
                    val r = radius + Perlin.noise3D(1.0 * k * r1, 1.0 * k * r2, 1.0 * i * config.circleGap.value)
                        .toFloat() * noisiness

                    val x = this.size.width / 2 + r * cos(Math.toRadians(theta.toDouble())).toFloat()
                    val y = this.size.height / 2 + r * sin(Math.toRadians(theta.toDouble())).toFloat()

                    if (bx == 0f && by == 0f) {
                        bx = x
                        by = y
                    }

                    drawLine(config.foreground.value, Offset(bx, by), Offset(x, y), 0.4f)
                    bx = x
                    by = y
                    theta += 1.0f
                }

            }
        }

    }

}
