package com.sumygg.anarts.arts.circleanddotgrid

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.math.sqrt

/**
 * 绘制CircleAndDotGrid
 */
class CircleAndDotGrid : BaseArts("CircleAndDotGrid") {

    private var angle by mutableStateOf(0f)
    private val outline = Stroke(width = 3f)

    override fun onInit() {

    }

    override fun onUpdate(elapsedNanos: Long) {
        angle += elapsedNanos * 0.0000000001f * 360f
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is CircleAndDotGridConfig) {
            throw IllegalArgumentException("${this.name} must use ${CircleAndDotGridConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            val n = 25
            val circleRadius = (size.width / n + n / 3)
            val spacing = circleRadius + n / 3
            val dotRadius = circleRadius / 7
            val diff = spacing - circleRadius
            val foreground = config.foreground.value
            val brush = SolidColor(foreground)
            val frameAngle = angle
            val circleExtent = circleRadius + 1.5f + 1f
            // The dot rotates around (x, y - spacing), rather than the circle center.
            val dotExtent = sqrt(diff * diff + circleRadius * circleRadius) + dotRadius + 1f
            val horizontalExtent = maxOf(circleExtent, dotExtent)
            val topExtent = maxOf(circleExtent, spacing + dotExtent)
            var offsetX = 0f
            var offsetY = 0f
            for (row in 0..n) {
                if (offsetY - topExtent > size.height) break
                for (col in 0..n) {
                    if (offsetX - horizontalExtent > size.width) break
                    if (offsetX - circleExtent <= size.width && offsetY - circleExtent <= size.height) {
                        drawCircle(
                            brush,
                            radius = circleRadius,
                            center = Offset(offsetX, offsetY),
                            style = outline
                        )
                    }
                    if (offsetX - dotExtent <= size.width && offsetY - spacing - dotExtent <= size.height &&
                        offsetY - spacing + dotExtent >= 0f) {
                        withTransform({
                            rotate(
                                (frameAngle + (row * col) + (col + row) * 15) % 360,
                                Offset(offsetX, offsetY - diff - circleRadius)
                            )
                        }) {
                            drawCircle(
                                color = foreground,
                                radius = dotRadius,
                                center = Offset(offsetX - diff, offsetY - diff)
                            )
                        }
                    }
                    offsetX += spacing
                }
                offsetX = 0f
                offsetY += spacing
            }
        }
    }


}
