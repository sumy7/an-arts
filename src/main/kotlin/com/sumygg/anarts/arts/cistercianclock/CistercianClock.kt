package com.sumygg.anarts.arts.cistercianclock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.arts.cisterciannumerals.cistercianNumeralsSymbol
import java.lang.Float.min
import java.time.LocalDateTime

/**
 * 绘制CistercianClock
 */
class CistercianClock : BaseArts("CistercianClock") {

    private var year by mutableStateOf(1990)
    private var date by mutableStateOf(101)
    private var time by mutableStateOf(0)
    private var seconds by mutableStateOf(0)

    override fun onInit() {
        this.onUpdate(0L)
    }

    override fun onUpdate(elapsedNanos: Long) {
        val d = LocalDateTime.now()
        year = d.year
        date = d.monthValue * 100 + d.dayOfMonth
        time = d.hour * 100 + d.minute
        seconds = d.second
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is CistercianClockConfig) {
            throw IllegalArgumentException("${this.name} must use ${CistercianClockConfig::class.simpleName}")
        }

        return {
            drawRect(config.background.value)

            val size = min(this.size.width / 13, this.size.height / 5).coerceAtLeast(50f)
            translate(this.size.width / 2 - 4.5f * size, this.size.height / 2) {
                cistercianNumeralsSymbol(year, size, config.foreground.value)
            }

            translate(this.size.width / 2 - 1.5f * size, this.size.height / 2) {
                cistercianNumeralsSymbol(date, size, config.foreground.value)
            }

            translate(this.size.width / 2 + 1.5f * size, this.size.height / 2) {
                cistercianNumeralsSymbol(time, size, config.foreground.value)
            }

            translate(this.size.width / 2 + 4.5f * size, this.size.height / 2) {
                cistercianNumeralsSymbol(seconds, size, config.foreground.value)
            }
        }
    }
}
