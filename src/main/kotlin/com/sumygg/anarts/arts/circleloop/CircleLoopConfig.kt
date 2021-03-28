package com.sumygg.anarts.arts.circleloop

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.SliderFormItem

/**
 * circleLoop表单配置项和数据定义
 */
class CircleLoopConfig : ArtsConfig() {

    /**
     * 半径
     */
    @get:SliderFormItem(title = "radius", min = 100f, max = 500f)
    val radius = mutableStateOf(100f)

}
