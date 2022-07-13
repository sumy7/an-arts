package com.sumygg.anarts.arts.contourline

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.Perlin
import kotlin.math.cos
import kotlin.math.sin

/**
 * 绘制 ContourLine
 */
class ContourLine : BaseArts("ContourLine") {

    private val lineNum = 500

    override fun onInit() {

    }

    override fun onUpdate(elapsedNanos: Long) {

    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is ContourLineConfig) {
            throw IllegalArgumentException("${this.name} must use ${ContourLineConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)
            // FIXME 循环次数过多，需要更高效的绘制方式
            for (i in 0 until lineNum) {
                val cls = config.colors[(0 until config.colors.size).random()]
                var x = Math.random() * this.size.width
                var y = Math.random() * this.size.height

                for (j in 0 until 1500) {
                    val theta = Perlin.noise2D(x / 800.0, y / 800.0) * Math.PI * 2 * 800
                    x += cos(theta) * 0.4
                    y += sin(theta) * 0.4

                    drawCircle(cls, 2.0f, Offset(x.toFloat(), y.toFloat()))

                    if (x > this.size.width || x < 0 || y > this.size.height || y < 0) {
                        x = Math.random() * this.size.width
                        y = Math.random() * this.size.height
                    }
                }
            }
        }
    }
}
