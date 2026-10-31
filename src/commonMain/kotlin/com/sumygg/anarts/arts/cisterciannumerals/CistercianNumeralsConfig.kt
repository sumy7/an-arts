package com.sumygg.anarts.arts.cisterciannumerals

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * CistercianNumerals 表单配置项
 */
class CistercianNumeralsConfig : ArtsConfig() {

    /**
     * 背景色
     */
    val background = mutableStateOf(Color.Black)

    /**
     * 前景色
     */
    val foreground = mutableStateOf(Color.Green)

    /**
     * 数字
     */
    val number = mutableStateOf(1234)

    override val fields = listOf(
        FormField.ColorChoose(title = "background", state = background),
        FormField.ColorChoose(title = "foreground", state = foreground),
        FormField.IntSlider(title = "number", min = 0f, max = 9999f, steps = 9998, state = number),
    )
}
