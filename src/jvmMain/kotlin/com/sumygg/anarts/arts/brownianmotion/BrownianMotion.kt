package com.sumygg.anarts.arts.brownianmotion

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

class BrownianMotion : BaseArts("BrownianMotion") {

    private val num = 2000
    private val range = 30

    private val ax = mutableStateListOf<Float>()
    private val ay = mutableStateListOf<Float>()

    private var elapsedCount = 0.0

    override fun onInit() {
        for (i in 0 until num) {
            ax.add(300f)
            ay.add(300f)
        }
    }

    override fun onUpdate(elapsedNanos: Long) {
        elapsedCount += elapsedNanos * 0.000001
        if (elapsedCount < 30) {
            return
        }
        elapsedCount -= 30

        for (i in 1 until num) {
            ax[i - 1] = ax[i]
            ay[i - 1] = ay[i]
        }

        ax[num - 1] = ax[num - 1] + (-range..range).random()
        ay[num - 1] = ay[num - 1] + (-range..range).random()
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is BrownianMotionConfig) {
            throw IllegalArgumentException("${this.name} must use ${BrownianMotionConfig::class.simpleName}")
        }

        return {
            drawRect(Color(0xFF333333))
            for (i in 1 until num) {
                val color = Color(255, 255, 255, 255 * i / num)
                drawLine(color, Offset(ax[i - 1], ay[i - 1]), Offset(ax[i], ay[i]), 2.0f)
            }
        }
    }
}