package com.sumygg.anarts.performance

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeCanvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.arts.brownianmotion.*
import com.sumygg.anarts.arts.circleanddotgrid.*
import com.sumygg.anarts.arts.circlegridscale.*
import com.sumygg.anarts.arts.snowflakes.*
import com.sumygg.anarts.performance.reference.*
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Surface
import kotlin.random.Random
import kotlin.test.*

internal fun raster(width: Int, height: Int, draw: DrawScope.() -> Unit): ByteArray =
    Surface.makeRasterN32Premul(width, height).use { surface ->
        CanvasDrawScope().draw(Density(1f), LayoutDirection.Ltr, surface.canvas.asComposeCanvas(), Size(width.toFloat(), height.toFloat()), draw)
        Bitmap().use { bitmap ->
            bitmap.allocN32Pixels(width, height)
            check(surface.readPixels(bitmap, 0, 0))
            checkNotNull(bitmap.readPixels())
        }
    }

class AnimationRenderingTest {
    @Test
    fun primitiveBrownianHistoryStillInvalidatesTheCanvasWhenItMoves() {
        val art = BrownianMotion(Random(50)).also { it.onInit() }
        val draw = art.onDraw(BrownianMotionConfig())
        var invalidations = 0
        val observer = SnapshotStateObserver { it() }
        observer.start()
        try {
            observer.observeReads(Unit, { invalidations++ }) { raster(80, 80, draw) }
            art.onUpdate(10_000_000L)
            Snapshot.sendApplyNotifications()
            assertEquals(0, invalidations)
            art.onUpdate(20_000_000L)
            Snapshot.sendApplyNotifications()
            assertEquals(1, invalidations)
        } finally {
            observer.stop()
            observer.clear()
        }
    }

    @Test
    fun circleFramesMatchBeforeOptimizationAtEdgesAndAcrossAnimationPhases() {
        val dimensions = listOf(79 to 91, 390 to 844, 601 to 599, 1200 to 680)
        for ((width, height) in dimensions) {
            for (foreground in listOf(Color.Black, Color(0.2f, 0.6f, 0.9f, 0.35f))) {
                val scaleConfig = CircleGridScaleConfig().also { it.foreground.value = foreground }
                val dotConfig = CircleAndDotGridConfig().also { it.foreground.value = foreground }
                assertMatchingFrames(CircleGridScale(), ReferenceCircleGridScale(), scaleConfig, width, height)
                assertMatchingFrames(CircleAndDotGrid(), ReferenceCircleAndDotGrid(), dotConfig, width, height)
            }
        }
    }

    @Test
    fun brownianHistoryFadeAndThirtyMillisecondCadenceMatchReferenceAcrossWraps() {
        val actual = BrownianMotion(Random(734))
        val reference = ReferenceBrownianMotion(Random(734))
        val config = BrownianMotionConfig()
        actual.onInit(); reference.onInit()
        // Includes sub-threshold/long frames and two complete wraps of the full 2,000-point history.
        val elapsed = longArrayOf(10_000_000, 19_000_000, 1_000_000, 60_000_000, 5_000_000)
        val checkpoints = setOf(0, 2, 12, 65, 130, 1999, 2001, 4001)
        for (frame in 0..4001) {
            val delta = if (frame < elapsed.size) elapsed[frame] else 30_000_000L
            actual.onUpdate(delta); reference.onUpdate(delta)
            if (frame in checkpoints) {
                assertContentEquals(raster(640, 640, reference.onDraw(config)), raster(640, 640, actual.onDraw(config)), "Brownian frame $frame")
            }
        }
    }

    @Test
    fun snowRemovalPreservesParticleOrderPositionsAndPixels() {
        val art = SnowFlakes()
        art.onInit()
        raster(400, 300, art.onDraw(SnowFlakesConfig()))
        val field = SnowFlakes::class.java.getDeclaredField("snowflakes").also { it.isAccessible = true }
        @Suppress("UNCHECKED_CAST")
        val particles = field.get(art) as MutableList<SnowFlake>
        val first = SnowFlake(400f).also { flake -> repeat(40) { flake.update(0L, 400f, 300f) } }
        val dead = SnowFlake(400f).also { it.update(0L, 400f, -100f) }
        val last = SnowFlake(400f).also { flake -> repeat(40) { flake.update(0L, 400f, 300f) } }
        particles.addAll(listOf(first, dead, last))
        art.onUpdate(0L) // No birth batch; each existing flake still moves exactly once.
        assertEquals(listOf(first, last), field.get(art))
        val expected = raster(400, 300) {
            drawRect(Color(0xFFA52A2A))
            for (flake in listOf(first, last)) drawCircle(Color.White, flake.size, Offset(flake.posX, flake.posY))
        }
        assertContentEquals(expected, raster(400, 300, art.onDraw(SnowFlakesConfig())))
    }

    private fun assertMatchingFrames(actual: BaseArts, reference: BaseArts, config: ArtsConfig, width: Int, height: Int) {
        actual.onInit(); reference.onInit()
        for (delta in longArrayOf(0, 16_666_667, 400_000_000, 700_000_000, 500_000_000, 1_000_000_000)) {
            actual.onUpdate(delta); reference.onUpdate(delta)
            assertContentEquals(raster(width, height, reference.onDraw(config)), raster(width, height, actual.onDraw(config)), "${actual.name}: ${width}x$height, delta=$delta")
        }
    }
}
