package com.sumygg.anarts.arts.cistercianclock

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * CistercianClock 表单配置项
 */
class CistercianClockConfig : ArtsConfig() {

    /**
     * 背景色
     */
    val background = mutableStateOf(Color.Black)

    /**
     * 前景色
     */
    val foreground = mutableStateOf(Color.Green)

    override val fields = listOf(
        FormField.ColorChoose(title = "background", state = background),
        FormField.ColorChoose(title = "foreground", state = foreground),
    )
}
