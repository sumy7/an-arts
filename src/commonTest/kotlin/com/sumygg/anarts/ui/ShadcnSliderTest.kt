package com.sumygg.anarts.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import com.sumygg.anarts.arts.cisterciannumerals.CistercianNumeralsConfig
import com.sumygg.anarts.ui.formitem.FormField
import kotlin.math.roundToInt

class ShadcnSliderTest {
    @Test
    fun continuousValuesClampAtLimitsWithoutLosingPrecision() {
        assertEquals(0.1234f, snapSliderValue(.1234f, 0f..1f, 0))
        assertEquals(-10f, snapSliderValue(-20f, -10f..10f, 0))
        assertEquals(10f, snapSliderValue(20f, -10f..10f, 0))
    }

    @Test
    fun discreteValuesSnapToIntervalsIncludingBothEndpoints() {
        assertEquals(5f, snapSliderValue(4.9f, 0f..10f, 3))
        assertEquals(0f, snapSliderValue(-1f, 0f..10f, 3))
        assertEquals(10f, snapSliderValue(11f, 0f..10f, 3))
        assertEquals(128f, snapSliderValue(127.8f, 0f..255f, 254))
    }

    @Test
    fun numeralSliderKeyboardStepsReachEveryInteger() {
        val field = CistercianNumeralsConfig().fields.filterIsInstance<FormField.IntSlider>().single()
        val increment = (field.max - field.min) / (field.steps + 1)
        var value = 0
        repeat(10) { value = snapSliderValue(value + increment, field.min..field.max, field.steps).roundToInt() }
        assertEquals(10, value)
        assertEquals(9999, snapSliderValue(field.max, field.min..field.max, field.steps).roundToInt())
    }
}
