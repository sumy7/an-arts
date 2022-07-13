package com.sumygg.anarts.arts.identicon

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.InputFormItem
import com.sumygg.anarts.ui.formitem.RadioFormItem

/**
 * Identicon 配置项
 */
class IdenticonConfig : ArtsConfig() {

    /**
     * 用于生成的文本
     */
    @get:InputFormItem(
        title = "text"
    )
    val text = mutableStateOf("")

    /**
     * 对文本hash使用的算法
     */
    @get:RadioFormItem(
        title = "hashType",
        values = ["SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"]
    )
    val hashType = mutableStateOf("SHA-256")


}
