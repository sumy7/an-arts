package com.sumygg.anarts.arts.circleloop

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem
import com.sumygg.anarts.ui.formitem.SliderFormItem

/**
 * circleLoop表单配置项和数据定义
 */
class CircleLoopConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color.Black)

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color(0x1EFFA500))

    /**
     * 半径
     */
    @get:SliderFormItem(title = "radius", min = 100f, max = 500f)
    val radius = mutableStateOf(100f)

    /**
     * 线宽
     */
    val lineWidth = mutableStateOf(1.0f)

    /**
     * 迭代次数
     */
    val iterations = mutableStateOf(1000)


}
