package com.sumygg.anarts.arts.dotswave

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * DotsWave表单配置项
 */
class DotsWaveConfig : ArtsConfig() {

    /**
     * 背景颜色
     */
    val background = mutableStateOf(Color.Black)

    /**
     * 颜色系列
     */
    val colors = mutableStateListOf(
        Color(0xFFFFBE0B),
        Color(0xFFFB5607),
        Color(0xFFFF006E),
        Color(0xFF8338EC),
        Color(0xFF3A86FF),
    )

    override val fields = listOf(
        FormField.ColorChoose(title = "background", state = background),
        FormField.ColorList(title = "colors", state = colors),
    )
}
