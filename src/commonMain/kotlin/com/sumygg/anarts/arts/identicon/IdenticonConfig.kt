package com.sumygg.anarts.arts.identicon

import androidx.compose.runtime.mutableStateOf
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * Identicon 配置项
 */
class IdenticonConfig : ArtsConfig() {

    /**
     * 用于生成的文本
     */
    val text = mutableStateOf("")

    /**
     * 对文本hash使用的算法
     */
    val hashType = mutableStateOf("SHA-256")


    override val fields = listOf(
        FormField.Radio(title = "hashType", values = listOf("SHA-1", "SHA-224", "SHA-256", "SHA-384", "SHA-512"), state = hashType),
        FormField.Input(title = "text", state = text),
    )
}
