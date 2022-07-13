package com.sumygg.anarts.arts.dotswave

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.Perlin
import com.sumygg.anarts.utils.random
import kotlin.math.abs
import kotlin.math.sin

/**
 * 绘制 DotsWave
 */
class DotsWave : BaseArts("DotsWave") {

    private val n = 300

    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is DotsWaveConfig) {
            throw IllegalArgumentException("${this.name} must use ${DotsWaveConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            for (i in 0 until n) {
                val x = (-0.1f..1.1f).random() * this.size.width
                val y = (-0.1f..1.1f).random() * this.size.height

                val num = (100..1000).random()
                val r = Math.random() * this.size.width * 0.15 * Math.random()
                val ind = (1..8).random()

                translate(x, y) {
                    rotate(((0..8).random() * Math.PI / 4).toFloat()) {
                        val shuffledColorIndexList = (0 until config.colors.size).shuffled()
                        for (j in 0 until num step ind) {
                            val s =
                                this.size.width * 0.15 * (0.0..(0.0..(0.0..(0.0..(0.0..(0.0..Math.random()).random()).random()).random()).random()).random()).random()
                            val ci = (config.colors.size * abs(
                                Perlin.noise3D(
                                    j * 0.01,
                                    x.toDouble(),
                                    y.toDouble()
                                )
                            )).toInt()
                            drawCircle(
                                color = config.colors[shuffledColorIndexList[ci]],
                                radius = (s * 2 / 3).toFloat(),
                                center = Offset(j.toFloat(), (r * sin(j * 0.05)).toFloat())
                            )
                        }
                    }
                }
            }
        }
    }
}
