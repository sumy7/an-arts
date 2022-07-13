package com.sumygg.anarts.arts.cisterciannumerals

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.math.min

/**
 * 绘制CistercianNumerals
 */
class CistercianNumerals : BaseArts("CistercianNumerals") {
    override fun onInit() {

    }

    override fun onUpdate(elapsedNanos: Long) {

    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is CistercianNumeralsConfig) {
            throw IllegalArgumentException("${this.name} must use ${CistercianNumeralsConfig::class.simpleName}")
        }

        return {
            drawRect(Color.Black)
            translate(this.size.width / 2, this.size.height / 2) {
                cistercianNumeralsSymbol(
                    config.number.value,
                    min((this.size.width - 100f) / 3, (this.size.height - 100f) / 3).coerceAtLeast(50f),
                    config.foreground.value
                )
            }
        }
    }
}
