package com.sumygg.anarts.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import kotlin.reflect.full.createInstance

/**
 * 保存Arts的模型类，包括Arts类型、Arts动态参数和Arts实现
 */
class ArtsModel(defaultArts: Arts) {
    var artsType: Arts by mutableStateOf(defaultArts)
        private set

    var artsConfig: ArtsConfig by mutableStateOf(defaultArts.artsConfig.createInstance())
        private set

    var arts: BaseArts by mutableStateOf(defaultArts.arts.createInstance())
        private set

    init {
        // 触发Arts初始化
        this.arts.onInit()
    }

    /**
     * 将当前Arts切换到新的类型
     *
     * @param artsType 新类型Arts
     */
    fun switchTo(artsType: Arts) {
        this.artsType = artsType
        this.artsConfig = artsType.artsConfig.createInstance()
        val arts = artsType.arts.createInstance()
        arts.onInit()
        this.arts = arts
    }

}
