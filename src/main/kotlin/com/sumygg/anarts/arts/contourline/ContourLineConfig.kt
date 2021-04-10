package com.sumygg.anarts.arts.contourline

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.ColorChooseFormItem
import com.sumygg.anarts.ui.formitem.ColorListFormItem

/**
 * ContourLine 表单配置项
 */
class ContourLineConfig : ArtsConfig() {

    /**
     * 背景颜色
     */
    @get:ColorChooseFormItem(title = "background")
    val background = mutableStateOf(Color(0xFF1A0633))

    /**
     * 线条颜色系列
     */
    @get:ColorListFormItem(title = "colors")
    val colors = mutableStateListOf(
        Color(0xFF581845),
        Color(0xFF900C3F),
        Color(0xFFC70039),
        Color(0xFFFF5733),
        Color(0xFFFFC30F)
    )

}
