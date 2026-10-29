package com.sumygg.anarts.arts.hearts

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.ui.formitem.FormField

/**
 * Hearts 配置项
 */
class HeartsConfig : ArtsConfig() {

    /**
     * 颜色系列
     */
    val colors = mutableStateListOf(
        Color(0xFFE03776),
        Color(0xFF8F3E98),
        Color(0xFF4687BF),
        Color(0xFF3BAB6F),
        Color(0xFFF9C25E),
        Color(0xFFF47274),
    )

    override val fields = listOf(
        FormField.ColorList(title = "colors", state = colors),
    )
}
