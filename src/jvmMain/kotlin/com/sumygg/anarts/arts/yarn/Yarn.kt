package com.sumygg.anarts.arts.yarn

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.Perlin

/**
 * 绘制 Yarn
 */
class Yarn : BaseArts("Yarn") {
    override fun onInit() {

    }

    override fun onUpdate(elapsedNanos: Long) {

    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is YarnConfig) {
            throw IllegalArgumentException("${this.name} must use ${YarnConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            translate(this.size.width / 2, this.size.height / 2) {
                var offset = 0.0
                val inc = 0.005
                for (i in 0 until config.n.value) {
                    val x0 = this.size.width * Perlin.noise1D(offset + 15)
                    val x1 = this.size.width * Perlin.noise1D(offset + 25)
                    val x2 = this.size.width * Perlin.noise1D(offset + 35)
                    val x3 = this.size.width * Perlin.noise1D(offset + 45)
                    val y0 = this.size.height * Perlin.noise1D(offset + 55)
                    val y1 = this.size.height * Perlin.noise1D(offset + 65)
                    val y2 = this.size.height * Perlin.noise1D(offset + 75)
                    val y3 = this.size.height * Perlin.noise1D(offset + 85)
                    val path = Path()
                    path.moveTo(x0.toFloat(), y0.toFloat())
                    path.cubicTo(
                        x1.toFloat(), y1.toFloat(),
                        x2.toFloat(), y2.toFloat(),
                        x3.toFloat(), y3.toFloat()
                    )

                    drawPath(
                        path, config.foreground.value,
                        style = Stroke(width = config.lineWidth.value)
                    )

                    offset += inc
                }
            }
        }
    }
}
