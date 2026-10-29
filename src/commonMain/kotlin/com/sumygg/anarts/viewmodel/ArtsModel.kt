package com.sumygg.anarts.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

/**
 * 保存Arts的模型类，包括Arts类型、Arts动态参数和Arts实现
 */
class ArtsModel(defaultArts: Arts) {
    var artsType: Arts by mutableStateOf(defaultArts)
        private set

    var artsConfig: ArtsConfig by mutableStateOf(defaultArts.createConfig())
        private set

    var arts: BaseArts by mutableStateOf(defaultArts.createArts())
        private set

    var isPlaying: Boolean by mutableStateOf(defaultArts.animated)
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
        this.artsConfig = artsType.createConfig()
        val arts = artsType.createArts()
        arts.onInit()
        this.arts = arts
        isPlaying = artsType.animated
    }

    fun togglePlayback() {
        if (artsType.animated) isPlaying = !isPlaying
    }

    /** Restores this artwork's defaults without resuming a paused animation. */
    fun resetCurrent() {
        val wasPlaying = isPlaying
        switchTo(artsType)
        isPlaying = wasPlaying
    }
}
