package com.sumygg.anarts.performance

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.asComposeCanvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.arts.brownianmotion.*
import com.sumygg.anarts.arts.circleanddotgrid.*
import com.sumygg.anarts.arts.circlegridscale.*
import com.sumygg.anarts.arts.snowflakes.*
import com.sumygg.anarts.performance.reference.*
import org.jetbrains.skia.Surface
import java.lang.management.ManagementFactory
import java.util.Locale
import kotlin.math.sin
import kotlin.random.Random

/** Standalone diagnostic, not a timing assertion in the regression suite. */
fun main() {
    val allocation = ManagementFactory.getThreadMXBean() as com.sun.management.ThreadMXBean
    allocation.isThreadAllocatedMemoryEnabled = true
    val thread = Thread.currentThread().id
    fun measure(label: String, iterations: Int, warmup: Int = 40, action: () -> Unit) {
        repeat(warmup) { action() }
        val timings = mutableListOf<Double>()
        val allocations = mutableListOf<Double>()
        repeat(3) {
            val bytes = allocation.getThreadAllocatedBytes(thread)
            val start = System.nanoTime()
            repeat(iterations) { action() }
            timings += (System.nanoTime() - start) / iterations / 1e6
            allocations += (allocation.getThreadAllocatedBytes(thread) - bytes) / iterations / 1024.0
        }
        println(String.format(Locale.ROOT, "%s: median %.6f ms/op, %.3f KiB/op", label, timings.sorted()[1], allocations.sorted()[1]))
    }

    val before = ReferenceBrownianMotion(Random(734), snapshotStorage = true).also { it.onInit() }
    val after = BrownianMotion(Random(734)).also { it.onInit() }
    // Populate a varied full history directly, avoiding 2,000 slow setup updates in the old renderer.
    val points = List(2000) { 300f + sin(it * .07).toFloat() * 100f }
    for (name in listOf("ax", "ay")) {
        val field = before.javaClass.getDeclaredField(name).also { it.isAccessible = true }
        @Suppress("UNCHECKED_CAST") val history = field.get(before) as MutableList<Float>
        history.clear(); history.addAll(points)
    }
    val trail = after.javaClass.getDeclaredField("trail").also { it.isAccessible = true }.get(after) as BrownianTrail
    for (point in points) trail.append(point, point)
    measure("Brownian update before", 100, 20) { before.onUpdate(33_333_333L) }
    measure("Brownian update after", 100, 20) { after.onUpdate(33_333_333L) }

    for ((label, art) in listOf("Snow update before" to ReferenceSnowFlakes(), "Snow update after" to SnowFlakes())) {
        art.onInit()
        val height = art.javaClass.getDeclaredField("height").also { it.isAccessible = true }
        height.setFloat(art, Float.MAX_VALUE)
        val field = art.javaClass.getDeclaredField("snowflakes").also { it.isAccessible = true }
        @Suppress("UNCHECKED_CAST") val particles = field.get(art) as MutableList<Any>
        repeat(500) { particles.add(if (art is SnowFlakes) SnowFlake(400f) else ReferenceSnowFlake(400f)) }
        // A zero delta retains the original per-frame y step, with no births/deaths in this probe.
        measure(label, 1000, 200) { art.onUpdate(0L) }
    }

    fun draw(label: String, art: BaseArts, config: ArtsConfig, side: Int) {
        art.onInit(); art.onUpdate(500_000_000L)
        Surface.makeRasterN32Premul(side, side).use { surface ->
            val canvas = surface.canvas.asComposeCanvas()
            val scope = CanvasDrawScope()
            val render = art.onDraw(config)
            measure("$label raster ${side}px", 100) {
                scope.draw(Density(1f), LayoutDirection.Ltr, canvas, Size(side.toFloat(), side.toFloat()), render)
            }
        }
    }
    for (side in listOf(390, 600, 1200)) {
        draw("CircleGridScale before", ReferenceCircleGridScale(), CircleGridScaleConfig(), side)
        draw("CircleGridScale after", CircleGridScale(), CircleGridScaleConfig(), side)
        draw("CircleAndDotGrid before", ReferenceCircleAndDotGrid(), CircleAndDotGridConfig(), side)
        draw("CircleAndDotGrid after", CircleAndDotGrid(), CircleAndDotGridConfig(), side)
    }
}
