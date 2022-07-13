package com.sumygg.anarts.arts.cisterciannumerals

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem
import com.sumygg.anarts.ui.formitem.SliderFormItem

/**
 * CistercianNumerals 表单配置项
 */
class CistercianNumeralsConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color.Black)

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color.Green)

    /**
     * 数字
     */
    @get:SliderFormItem(title = "number", min = 0f, max = 9999f, steps = 9999, fixToInt = true)
    val number = mutableStateOf(1234)

}
