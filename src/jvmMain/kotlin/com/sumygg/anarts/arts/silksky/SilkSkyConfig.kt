package com.sumygg.anarts.arts.silksky

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig

/**
 * SilkSky 配置项
 */
class SilkSkyConfig : ArtsConfig() {

    /**
     * 颜色层级数量
     */
    val circleNum = mutableStateOf(15)

    /**
     * 中心空白圆半径
     */
    val sunRadius = mutableStateOf(5.0f)

}
