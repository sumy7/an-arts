package com.sumygg.anarts.arts

import com.sumygg.anarts.arts.snowflakes.SnowFlake
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SnowFlakesRegressionTest {
    @Test
    fun verticalMotionKeepsTheOriginalPerUpdateStepAtEveryElapsedTime() {
        val flake = SnowFlake(400f)
        val step = flake.size.pow(0.5f)
        var expectedY = flake.posY

        for (elapsedMillis in listOf(0L, 1L, 16L, 250L, 0L)) {
            expectedY += step
            flake.update(elapsedMillis, 400f, 10_000f)
            assertEquals(expectedY, flake.posY)
            assertTrue(flake.alive)
        }
    }

    @Test
    fun particleDiesOnlyAfterPassingTheBottomAndNeverResurrects() {
        val flake = SnowFlake(400f)
        val bottom = flake.posY + flake.size.pow(0.5f)

        flake.update(16L, 400f, bottom)
        assertEquals(bottom, flake.posY)
        assertTrue(flake.alive)

        flake.update(16L, 400f, bottom)
        assertFalse(flake.alive)
        flake.update(16L, 400f, 10_000f)
        assertFalse(flake.alive)
    }

    @Test
    fun horizontalOrbitKeepsItsBirthWidthWhenTheCanvasChanges() {
        // A zero-width birth has zero orbit radius and remains centered at zero.
        val flake = SnowFlake(0f)
        for (canvasWidth in listOf(400f, 1_200f, 80f)) {
            flake.update(500L, canvasWidth, 10_000f)
            assertEquals(0f, flake.posX)
        }
    }

    @Test
    fun zeroElapsedTimeKeepsOrbitPositionButStillAdvancesVerticalMotion() {
        val flake = SnowFlake(400f)
        flake.update(32L, 400f, 10_000f)
        val x = flake.posX
        val y = flake.posY

        flake.update(0L, 400f, 10_000f)
        assertEquals(x, flake.posX)
        assertEquals(y + flake.size.pow(0.5f), flake.posY)
    }
}
