package com.sumygg.anarts.arts.maze

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem

/**
 * maze 表单项配置和数据定义
 */
class MazeConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color(0xFFF0FFFF))

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color(0xFFFFA500))

    /**
     * 线宽度
     */
    val lineWidth = mutableStateOf(3.0f)

    /**
     * 步长
     */
    val step = mutableStateOf(20)
}
