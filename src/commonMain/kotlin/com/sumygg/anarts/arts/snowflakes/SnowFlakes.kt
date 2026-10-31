package com.sumygg.anarts.arts.snowflakes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.random
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * 单个Snow对象
 */
class SnowFlake(private val width: Float) {
    var alive = true
        private set
    private var liveTime = 0L
    var posX = 0f
        private set
    var posY = (-50f..0f).random()
        private set
    private val initialangle = (0.0..(2 * kotlin.math.PI)).random()
    val size = (2.0f..5.0f).random()

    // radius of snowflake spiral
    // chosen so the snowflakes are uniformly spread out in area
    private val radius = sqrt((0f..(width / 2) * (width / 2)).random().toDouble())
    private val fallStep = size.pow(0.5f)

    fun update(time: Long, width: Float, height: Float) {
        liveTime += time
        // x position follows a circle
        val w = 0.6 // angular speed
        val angle = 1.0f * w * liveTime / 1000 + initialangle
        this.posX = (this.width / 2 + this.radius * sin(angle)).toFloat()

        // different size snowflakes fall at slightly different y speeds
        this.posY += fallStep

        // delete snowflake if past end of screen
        if (this.posY > height) {
            this.alive = false
        }
    }
}

/**
 * 绘制 SnowFlake
 */
class SnowFlakes : BaseArts("SnowFlakes") {

    private val snowflakes = ArrayList<SnowFlake>()

    private var width = 400f
    private var height = 600f

    private var times by mutableStateOf(0L)

    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
        val elapsedMillis = elapsedNanos / 1000000
        for (snowflake in snowflakes) {
            snowflake.update(elapsedMillis, this.width, this.height)
        }

        times += elapsedMillis
        if (times > 1000 / 60) {
            times -= 1000 / 60
            repeat((0..5).random()) {
                snowflakes.add(SnowFlake(this.width))
            }
        }

        // Compact in drawing order and keep the backing array for the next frame.
        var writeIndex = 0
        for (readIndex in snowflakes.indices) {
            val snowflake = snowflakes[readIndex]
            if (snowflake.alive) {
                if (writeIndex != readIndex) snowflakes[writeIndex] = snowflake
                writeIndex++
            }
        }
        while (snowflakes.size > writeIndex) snowflakes.removeAt(snowflakes.lastIndex)
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {

        return {
            this@SnowFlakes.width = this.size.width
            this@SnowFlakes.height = this.size.height
            times

            drawRect(Color(0xFFA52A2A))

            for (snowflake in snowflakes) {
                drawCircle(Color.White, snowflake.size, Offset(snowflake.posX, snowflake.posY))
            }
        }

    }
}
