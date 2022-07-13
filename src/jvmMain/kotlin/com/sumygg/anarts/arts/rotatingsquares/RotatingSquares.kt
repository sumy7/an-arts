package com.sumygg.anarts.arts.rotatingsquares

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

data class Rect(
    val size: Dp,
    val offsetX: Dp,
    val offsetY: Dp,
    val color: Color
)

/**
 * 绘制RotatingSquares
 */
class RotatingSquares : BaseArts("RotatingSquares") {

    private val n = 35
    private val rectList = mutableListOf<Rect>()
    private var angle by mutableStateOf(0f)

    override fun onInit() {
        val rectSize = 15.dp
        val colorList = listOf(
            Color(red = 40, green = 223, blue = 153),
            Color(red = 210, green = 246, blue = 197),
            Color(red = 246, green = 247, blue = 212)
        )
        for (i in 0..n) {
            val s = rectSize * i
            val offsetX = -s / 2
            val offsetY = -s / 2
            val rect = Rect(s, offsetX, offsetY, colorList[i % colorList.size])
            rectList.add(rect)
        }
    }

    override fun onUpdate(elapsedNanos: Long) {
        angle += elapsedNanos * 0.0000000001f * 360f
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is RotatingSquaresConfig) {
            throw IllegalArgumentException("${this.name} must use ${RotatingSquaresConfig::class.simpleName}")
        }

        return {
            translate(size.width / 2, size.height / 2) {
                for (i in n downTo 0) {
                    rotate(angle * (n - i + 1) * 0.07f, Offset(0f, 0f)) {
                        drawRect(
                            brush = SolidColor(rectList[i].color),
                            Offset(rectList[i].offsetX.toPx(), rectList[i].offsetY.toPx()),
                            Size(rectList[i].size.toPx(), rectList[i].size.toPx()),
                            alpha = 0.7f
                        )
                    }
                }
            }
        }
    }
}
