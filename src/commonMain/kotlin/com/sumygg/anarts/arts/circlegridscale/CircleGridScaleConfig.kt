package com.sumygg.anarts.arts.circlegridscale

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * CircleGridScale 表单项配置定义
 */
class CircleGridScaleConfig : ArtsConfig() {

    /**
     * 背景色
     */
    val background = mutableStateOf(Color.White)

    /**
     * 前景色
     */
    val foreground = mutableStateOf(Color.Black)

    override val fields = listOf(
        FormField.ColorChoose(title = "background", state = background),
        FormField.ColorChoose(title = "foreground", state = foreground),
    )
}
