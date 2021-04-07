package com.sumygg.anarts.arts.voidsetup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.*
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.math.PI

/**
 * 绘制 VoidSetup
 */
class VoidSetup : BaseArts("VoidSetup") {

    private var a: Float by mutableStateOf(0f)

    private val canvasSize = 400f

    override fun onInit() {
        a = 0f
    }

    override fun onUpdate(elapsedNanos: Long) {
        a += elapsedNanos * 0.0000000001f * 40f
        if (a > 30) {
            a -= 50
        }
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {

        return {
            drawRect(Color.White)
            translate(this.size.width / 2 - canvasSize / 2, this.size.height / 2 - canvasSize / 2) {
                clipRect(0f, 0f, canvasSize, canvasSize) {
                    drawRect(
                        Color(0xFF021050),
                        topLeft = Offset.Zero,
                        size = Size(canvasSize, canvasSize)
                    )
                    translate(canvasSize / 2 + a * 10, canvasSize / 2 + a * 10) {
                        rotate(Math.toDegrees(a.toDouble()).toFloat(), Offset.Zero) {
                            scale(a / 15, Offset.Zero) {
                                kao(0, -50f)
                                kao(1, 50f)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun DrawScope.kao(don: Int, b: Float) {
        drawCircle(
            color = Color(0xFFE7EEDD),
            radius = 50f / 2,
            center = Offset(b, 0f)
        )
        if (don == 0) {
            drawCircle(
                color = Color(0xFF68C0C0),
                radius = 38f / 2,
                center = Offset(b, 0f),
            )
        } else {
            drawCircle(
                color = Color(0xFFFB4828),
                radius = 38f / 2,
                center = Offset(b, 0f),
            )
        }
        drawCircle(
            color = Color.Black,
            radius = 50f / 2,
            center = Offset(b, 0f),
            style = Stroke(width = 2.0f)
        )

        val path = Path()
        path.arcToRad(
            Rect(b + 4.26f - 8.5f / 2, 5f - 7.3f / 2, b + 4.26f + 8.5f / 2, 5f + 7.3f / 2),
            0.3f,
            PI.toFloat() - 0.3f,
            false
        )
        path.arcToRad(
            Rect(b - 4.26f - 8.5f / 2, 5f - 7.3f / 2, b - 4.26f + 8.5f / 2, 5f + 7.3f / 2),
            0f,
            PI.toFloat() - 0.3f,
            false
        )
        drawPath(path, Color.Black, style = Stroke(1.2f))

        drawOval(
            color = Color.Black,
            topLeft = Offset(b - 11f - 6.56f / 2, -2f - 7f / 2),
            size = Size(6.56f, 7f)
        )

        drawOval(
            color = Color.Black,
            topLeft = Offset(b + 11f - 6.56f / 2, -2f - 7f / 2),
            size = Size(6.56f, 7f)
        )
    }
}
