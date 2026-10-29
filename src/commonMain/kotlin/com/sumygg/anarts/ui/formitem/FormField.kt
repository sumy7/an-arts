package com.sumygg.anarts.ui.formitem

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color

/** A form field and its live, typed configuration value, shared by desktop and web. */
sealed class FormField(val title: String) {
    class Text(title: String, val state: MutableState<String>) : FormField(title)
    class Input(title: String, val state: MutableState<String>) : FormField(title)
    class Slider(
        title: String,
        val min: Float,
        val max: Float,
        val steps: Int = 0,
        val state: MutableState<Float>,
    ) : FormField(title)
    class IntSlider(
        title: String,
        val min: Float,
        val max: Float,
        val steps: Int = 0,
        val state: MutableState<Int>,
    ) : FormField(title)
    class ColorChoose(title: String, val state: MutableState<Color>) : FormField(title)
    class Radio(
        title: String,
        val values: List<String>,
        val state: MutableState<String>,
    ) : FormField(title)
    class ColorList(
        title: String,
        val canEmpty: Boolean = false,
        val state: MutableList<Color>,
    ) : FormField(title)
}
