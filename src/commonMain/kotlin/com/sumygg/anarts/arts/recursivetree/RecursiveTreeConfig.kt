package com.sumygg.anarts.arts.recursivetree

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * RecursiveTree 表单项配置
 */
class RecursiveTreeConfig : ArtsConfig() {

    /**
     * 两个上下级树枝偏移的角度
     */
    val degrees = mutableStateOf(45f)

    override val fields = listOf(
        FormField.Slider("degrees", min = 0f, max = 90f, state = degrees),
    )
}
