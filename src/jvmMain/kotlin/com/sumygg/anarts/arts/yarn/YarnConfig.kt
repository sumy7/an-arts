package com.sumygg.anarts.arts.yarn

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem

/**
 * yarn配置
 */
class YarnConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color(0xFFFFA500))

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color(0x3C000000))

    /**
     * 线宽度
     */
    val lineWidth = mutableStateOf(0.3f)


    /**
     * 线段数量
     */
    val n = mutableStateOf(2000)
}
