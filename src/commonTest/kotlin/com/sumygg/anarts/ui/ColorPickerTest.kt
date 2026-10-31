package com.sumygg.anarts.ui

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.math.abs
import kotlin.test.assertTrue

class ColorPickerTest {
    @Test
    fun sixDigitHexPreservesCurrentAlphaAndEightDigitHexSetsIt() {
        val rgb = parsePickerHex(" #336699 ", 0.25f)!!
        assertEquals(0.2f, rgb.red, 0.001f)
        assertEquals(0.4f, rgb.green, 0.001f)
        assertEquals(0.6f, rgb.blue, 0.001f)
        assertEquals(0.25f, rgb.alpha, 0.001f)
        val rgba = parsePickerHex("33669980", 1f)!!
        assertEquals(128f / 255f, rgba.alpha, 0.001f)
    }

    @Test
    fun partialOrInvalidHexCannotChangeTheColor() {
        listOf("", "#", "fff", "12345", "1234567", "123456789", "#ffgg00", "-12345", "1234.6").forEach {
            assertNull(parsePickerHex(it, 1f), it)
        }
    }

    @Test
    fun channelsAcceptOnlyByteValues() {
        assertEquals(0f, parsePickerChannel("0"))
        assertEquals(1f, parsePickerChannel("255"))
        assertEquals(128f / 255f, parsePickerChannel("128"))
        listOf("", "-1", "256", "1.5", "test").forEach { assertNull(parsePickerChannel(it)) }
    }

    @Test
    fun rgbHsvRoundTripsSaturatedAndAchromaticColorsWithAlpha() {
        val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.White, Color.Black, Color.Gray, Color(0.9f, 0.2f, 0.6f, 0.3f), Color(0.1f, 0.7f, 0.8f, 0.5f))
        colors.forEach { color ->
            val hsv = color.pickerHsv()
            assertTrue(hsv.hue in 0f..360f)
            val result = hsv.color(color.alpha)
            assertTrue(abs(result.red - color.red) < 0.002f)
            assertTrue(abs(result.green - color.green) < 0.002f)
            assertTrue(abs(result.blue - color.blue) < 0.002f)
            assertEquals(color.alpha, result.alpha)
        }
    }

    @Test
    fun fullHueRangeWrapsToRedWithoutLosingOpacity() {
        val color = PickerHsv(360f, 1f, 1f).color(0.4f)
        assertEquals(1f, color.red, 0.001f)
        assertEquals(0f, color.green, 0.001f)
        assertEquals(0f, color.blue, 0.001f)
        assertEquals(0.4f, color.alpha, 0.001f)
    }
}
