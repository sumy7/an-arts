package com.sumygg.anarts.arts.hearts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

/**
 * 绘制 Hearts
 */
class Hearts : BaseArts("Hearts") {

    private var baseScale by mutableStateOf(0f)
    private val scaleMulti = 2.0f
    private var index = 0
    private val n = 18

    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
        baseScale += elapsedNanos * 0.000000001f
        if (baseScale > scaleMulti) {
            baseScale -= scaleMulti
            index += 1
        }
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is HeartsConfig) {
            throw IllegalArgumentException("${this.name} must use ${HeartsConfig::class.simpleName}")
        }

        return {
            translate(this.size.width / 2, this.size.height / 2) {
                for (i in n downTo 0) {
                    val scale = baseScale + i * scaleMulti
                    val path = Path()
                    path.moveTo(0.0f * scale, 12.0f * scale)
                    path.cubicTo(
                        50.0f * scale,
                        -30.0f * scale,
                        110.0f * scale,
                        50.0f * scale,
                        0.0f * scale,
                        120.0f * scale
                    )
                    path.cubicTo(
                        -110.0f * scale,
                        50.0f * scale,
                        -50.0f * scale,
                        -30.0f * scale,
                        0.0f * scale,
                        12.0f * scale
                    )
                    path.close()
                    path.translate(Offset(0.0f, -69.0f * scale))

                    drawPath(
                        path,
                        config.colors[(index + (config.colors.size - i % config.colors.size)) % config.colors.size]
                    )
                }
            }
        }
    }

}
