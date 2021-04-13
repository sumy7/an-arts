package com.sumygg.anarts.arts.silksky

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.distance
import com.sumygg.anarts.utils.hslToRgb
import com.sumygg.anarts.utils.random

/**
 * 绘制 SilkSky
 */
class SilkSky : BaseArts("SilkSky") {
    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is SilkSkyConfig) {
            throw IllegalArgumentException("${this.name} must use ${SilkSkyConfig::class.simpleName}")
        }

        return {
            val xm = (0f..(this.size.width / 5)).random() + this.size.width * 4 / 5 - this.size.width / 5
            val ym = (0f..(this.size.height / 5)).random() + this.size.height * 4 / 5 - this.size.height / 5

            val mh = config.circleNum.value * 2 + 2
            val ms = config.circleNum.value * 2 + 50
            val mv = 100

            for (i in 0 until config.circleNum.value) {
                for (j in 0 until config.circleNum.value) {
                    val (cr, cg, cb) = hslToRgb(
                        (config.circleNum.value + j) * 1.0f / mh,
                        (i + 50) * 1.0f / ms,
                        70.0f / mv
                    )
                    val rgba = Color(cr, cg, cb, 10)

                    val xn = (i + 0.5f) * this.size.width / config.circleNum.value
                    val yn = (j + 0.5f) * this.size.height / config.circleNum.value
                    val r = distance(xn.toDouble(), yn.toDouble(), xm.toDouble(), ym.toDouble()).toFloat()
                    drawCircle(rgba, r - config.sunRadius.value / 2, Offset(xn, yn))
                }
            }
        }
    }
}
