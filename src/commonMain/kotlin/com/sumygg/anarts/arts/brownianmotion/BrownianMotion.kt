package com.sumygg.anarts.arts.brownianmotion

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.random.Random

class BrownianMotion internal constructor(private val random: Random) : BaseArts("BrownianMotion") {

    constructor() : this(Random.Default)

    private val num = 2000
    private val range = 30

    private val trail = BrownianTrail(num)
    private val segmentColors = Array(num) { i -> Color(255, 255, 255, 255 * i / num) }
    private var revision by mutableIntStateOf(0)

    private var elapsedCount = 0.0

    override fun onInit() {
        trail.reset()
        elapsedCount = 0.0
        revision++
    }

    override fun onUpdate(elapsedNanos: Long) {
        elapsedCount += elapsedNanos * 0.000001
        if (elapsedCount < 30) {
            return
        }
        elapsedCount -= 30

        val latest = trail.pointAt(num - 1)
        trail.append(
            latest.x + random.nextInt(-range, range + 1),
            latest.y + random.nextInt(-range, range + 1),
        )
        revision++
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is BrownianMotionConfig) {
            throw IllegalArgumentException("${this.name} must use ${BrownianMotionConfig::class.simpleName}")
        }

        return {
            // Geometry is primitive storage; this single state read schedules the next draw.
            revision
            drawRect(Color(0xFF333333))
            var previous = trail.pointAt(0)
            for (i in 1 until num) {
                val next = trail.pointAt(i)
                drawLine(segmentColors[i], previous, next, 2.0f)
                previous = next
            }
        }
    }
}


/** Oldest-to-newest history without shifting or observing individual coordinates. */
internal class BrownianTrail(private val capacity: Int) {
    init { require(capacity > 0) }

    private val xs = FloatArray(capacity) { 300f }
    private val ys = FloatArray(capacity) { 300f }
    private var oldest = 0

    fun reset() {
        xs.fill(300f)
        ys.fill(300f)
        oldest = 0
    }

    fun append(x: Float, y: Float) {
        xs[oldest] = x
        ys[oldest] = y
        oldest = (oldest + 1) % capacity
    }

    fun pointAt(index: Int): Offset {
        val position = (oldest + index) % capacity
        return Offset(xs[position], ys[position])
    }
}
