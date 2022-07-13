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

/**
 * 绘制CircleAndDotGrid
 */
class CircleAndDotGrid : BaseArts("CircleAndDotGrid") {

    private var angle by mutableStateOf(0f)

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
            var offsetX = 0f
            var offsetY = 0f
            for (row in 0..n) {
                for (col in 0..n) {
                    drawCircle(
                        SolidColor(config.foreground.value),
                        radius = circleRadius,
                        center = Offset(offsetX, offsetY),
                        style = Stroke(width = 3f)
                    )
                    withTransform({
                        rotate(
                            (angle + (row * col) + (col + row) * 15) % 360,
                            Offset(offsetX, offsetY - diff - circleRadius)
                        )
                    }) {
                        drawCircle(
                            color = config.foreground.value,
                            radius = dotRadius,
                            center = Offset(offsetX - diff, offsetY - diff)
                        )
                    }
                    offsetX += spacing
                }
                offsetX = 0f
                offsetY += spacing
            }
        }
    }


}
