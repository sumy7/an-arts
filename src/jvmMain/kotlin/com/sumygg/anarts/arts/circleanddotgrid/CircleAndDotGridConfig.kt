package com.sumygg.anarts.arts.circleanddotgrid

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem

/**
 * CircleAndDotGrid 配置表单定义
 */
class CircleAndDotGridConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color.White)

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color.Black)
}
