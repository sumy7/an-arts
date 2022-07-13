package com.sumygg.anarts.arts.pointribbon

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig

/**
 * PointRibbon 配置项
 */
class PointRibbonConfig : ArtsConfig() {

    /**
     * 线宽度
     */
    val lineWidth = mutableStateOf(2.0f)

    /**
     * 半径
     */
    val r = mutableStateOf(50f)

}