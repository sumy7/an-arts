package com.sumygg.anarts

import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.arts.circleloop.CircleLoopConfig
import com.sumygg.anarts.viewmodel.ArtsModel
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class ArtsModelTest {
    @Test
    fun everyRegisteredArtworkInitializesAndCreatesADrawFunction() {
        assertEquals(19, Arts.entries.size)
        for (type in Arts.entries) {
            val model = ArtsModel(type)
            assertEquals(type, model.artsType)
            assertTrue(model.arts.name.isNotBlank())
            model.arts.onUpdate(16_666_667L)
            model.arts.onDraw(model.artsConfig)
        }
    }

    @Test
    fun switchingArtworkResetsItsConfigurationAndInstance() {
        val model = ArtsModel(Arts.CIRCLE_LOOP)
        val originalConfig = model.artsConfig as CircleLoopConfig
        val originalArt = model.arts
        val defaultRadius = originalConfig.radius.value
        originalConfig.radius.value = 123f
        model.switchTo(Arts.MAZE)
        assertEquals(Arts.MAZE, model.artsType)
        model.switchTo(Arts.CIRCLE_LOOP)
        assertNotSame(originalArt, model.arts)
        assertNotSame(originalConfig, model.artsConfig)
        assertEquals(defaultRadius, (model.artsConfig as CircleLoopConfig).radius.value)
    }

    @Test
    fun artworkMenuIsAlphabeticallySorted() {
        val names = Arts.sortedValues.map { it.artsName }
        assertEquals(names.sorted(), names)
        assertTrue(Arts.entries.any { it.animated })
        assertTrue(Arts.entries.any { !it.animated })
    }

    @Test
    fun playbackOnlyAppliesToAnimatedArtwork() {
        val model = ArtsModel(Arts.ROTATING_SQUARES)
        assertTrue(model.isPlaying)
        model.togglePlayback()
        assertFalse(model.isPlaying)
        model.togglePlayback()
        assertTrue(model.isPlaying)
        model.switchTo(Arts.MAZE)
        assertFalse(model.isPlaying)
        model.togglePlayback()
        assertFalse(model.isPlaying)
        model.switchTo(Arts.SNOWFLAKES)
        assertTrue(model.isPlaying)
    }

    @Test
    fun resettingArtworkRecreatesItAndPreservesPlaybackState() {
        val model = ArtsModel(Arts.ROTATING_SQUARES)
        val originalArt = model.arts
        val originalConfig = model.artsConfig
        model.togglePlayback()
        model.resetCurrent()
        assertEquals(Arts.ROTATING_SQUARES, model.artsType)
        assertNotSame(originalArt, model.arts)
        assertNotSame(originalConfig, model.artsConfig)
        assertFalse(model.isPlaying)
        model.togglePlayback()
        model.resetCurrent()
        assertTrue(model.isPlaying)
    }

    @Test
    fun resettingStaticArtworkRestoresDefaultParameters() {
        val model = ArtsModel(Arts.CIRCLE_LOOP)
        val originalConfig = model.artsConfig as CircleLoopConfig
        val defaultRadius = originalConfig.radius.value
        originalConfig.radius.value = 250f
        model.resetCurrent()
        assertEquals(defaultRadius, (model.artsConfig as CircleLoopConfig).radius.value)
        assertFalse(model.isPlaying)
    }
}
