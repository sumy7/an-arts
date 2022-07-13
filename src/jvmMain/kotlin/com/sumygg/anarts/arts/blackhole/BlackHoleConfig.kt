package com.sumygg.anarts.arts.blackhole

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem

/**
 * BlackHole 表单配置项
 */
class BlackHoleConfig : ArtsConfig() {

    /**
     * 背景色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color(30, 30, 30))

    /**
     * 前景色
     */
    @get:ColorChooseFormItem(title = "foreground")
    val foreground = mutableStateOf(Color(0xFFFF6347))

    /**
     * 圆环的数量
     */
    val circleN = mutableStateOf(200)

    /**
     * 圆环密度
     */
    val density = mutableStateOf(400f)

    /**
     * 圆环偏移量
     */
    val circleGap = mutableStateOf(0.01f)

}
