package com.sumygg.anarts.ui

import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.circleloop.CircleLoopConfig
import com.sumygg.anarts.arts.cisterciannumerals.CistercianNumeralsConfig
import com.sumygg.anarts.arts.hearts.HeartsConfig
import com.sumygg.anarts.arts.identicon.IdenticonConfig
import com.sumygg.anarts.arts.pointribbon.PointRibbonConfig
import com.sumygg.anarts.ui.formitem.FormField
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class FormConfigurationTest {
    @Test
    fun circleLoopExposesExistingControlsAndUpdatesDrawingState() {
        val config = CircleLoopConfig()
        assertEquals(listOf("background", "foreground", "radius"), config.fields.map { it.title })
        val background = assertIs<FormField.ColorChoose>(config.fields[0])
        background.state.value = Color.Blue
        assertEquals(Color.Blue, config.background.value)
        val radius = assertIs<FormField.Slider>(config.fields[2])
        assertEquals(100f, radius.min)
        assertEquals(500f, radius.max)
        radius.state.value = 250f
        assertEquals(250f, config.radius.value)
    }

    @Test
    fun integerControlKeepsItsRangeAndIntegerState() {
        val config = CistercianNumeralsConfig()
        val number = assertIs<FormField.IntSlider>(config.fields.last())
        assertEquals(0f, number.min)
        assertEquals(9999f, number.max)
        assertEquals(1f, (number.max - number.min) / (number.steps + 1))
        number.state.value = 9999
        assertEquals(9999, config.number.value)
    }

    @Test
    fun identiconKeepsAlgorithmChoicesAndAlphabeticalControlOrder() {
        val config = IdenticonConfig()
        assertEquals(listOf("hashType", "text"), config.fields.map { it.title })
        val algorithm = assertIs<FormField.Radio>(config.fields[0])
        assertEquals(listOf("SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"), algorithm.values)
        algorithm.state.value = "SHA-512"
        assertEquals("SHA-512", config.hashType.value)
        val text = assertIs<FormField.Input>(config.fields[1])
        text.state.value = "生成式艺术"
        assertEquals("生成式艺术", config.text.value)
    }

    @Test
    fun paletteFieldsShareLiveColorsAndKeepNonemptyConstraint() {
        val config = HeartsConfig()
        val colors = assertIs<FormField.ColorList>(config.fields.single())
        assertFalse(colors.canEmpty)
        colors.state[0] = Color(1f, 0f, 0f, 0.5f)
        assertEquals(Color(1f, 0f, 0f, 0.5f), config.colors[0])
        colors.state.add(Color.Blue)
        assertEquals(Color.Blue, config.colors.last())
    }

    @Test
    fun previouslyHiddenPropertiesRemainOutsideTheForm() {
        assertTrue(PointRibbonConfig().fields.isEmpty())
    }
}
