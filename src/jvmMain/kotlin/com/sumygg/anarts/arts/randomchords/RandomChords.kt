package com.sumygg.anarts.arts.randomchords

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.random
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class RandomChords : BaseArts("RandomChords") {
    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is RandomChordsConfig) {
            throw IllegalArgumentException("${this.name} must use ${RandomChordsConfig::class.simpleName}")
        }

        return {
            drawRect(Color.White)

            val radius = min(this.size.width, this.size.height) / 2

            translate(this.size.width / 2, this.size.height / 2) {
                for (i in 0..1000) {
                    val angle1 = (0.0..2.0 * PI).random()
                    val xpos1 = radius * cos(angle1)
                    val ypos1 = radius * sin(angle1)

                    val angle2 = (0.0..2.0 * PI).random()
                    val xpos2 = radius * cos(angle2)
                    val ypos2 = radius * sin(angle2)

                    drawLine(
                        Color.Black,
                        Offset(xpos1.toFloat(), ypos1.toFloat()),
                        Offset(xpos2.toFloat(), ypos2.toFloat())
                    )
                }
            }
        }

    }
}