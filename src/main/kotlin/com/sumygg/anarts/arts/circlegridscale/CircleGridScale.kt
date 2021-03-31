package com.sumygg.anarts.arts.circlegridscale

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

/**
 * 绘制 CircleGridScale
 */
class CircleGridScale : BaseArts("CircleGridScale") {

    private var scale by mutableStateOf(0f)
    private var timeElapsed = 0L
    private var reverse = false

    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
        timeElapsed = (timeElapsed + elapsedNanos)
        if (timeElapsed > 1500000000L) {
            reverse = !reverse
            timeElapsed -= 1500000000L
        }
        this.scale = if (reverse) {
            0.5f + (1.75f - 0.5f) * FastOutSlowInEasing.transform(timeElapsed.toFloat() / 1500000000L)
        } else {
            0.5f + (1.75f - 0.5f) * (1.0f - FastOutSlowInEasing.transform(timeElapsed.toFloat() / 1500000000L))
        }
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is CircleGridScaleConfig) {
            throw IllegalArgumentException("${this.name} must use ${CircleGridScaleConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            val radius = 50f
            val n = 25
            var colOffset: Float
            var rowOffset = 0f
            for (row in 1..n) {
                colOffset = if (row % 2 == 0) {
                    radius
                } else {
                    0f
                }
                for (col in 1..n) {
                    drawCircle(
                        SolidColor(config.foreground.value),
                        radius = radius * scale,
                        center = Offset(
                            colOffset,
                            rowOffset
                        ),
                        style = if (scale in 0.5f..0.95f) Fill else Stroke(width = 3f)
                    )
                    colOffset += 2 * radius
                }
                rowOffset += 2 * radius - 10f
            }
        }
    }
}
