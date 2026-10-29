package com.sumygg.anarts.utils

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals

class FormattingTest {
    @Test
    fun byteHexPreservesLeadingZerosAndUnsignedBytes() {
        assertEquals("00010f7f80ff", byteArrayOf(0, 1, 15, 127, -128, -1).toHexString())
        assertEquals("", byteArrayOf().toHexString())
    }

    @Test
    fun colorHexUsesRgbaOrderAndUppercase() {
        assertEquals("#FF0000FF", Color.Red.toHexString())
        assertEquals("#00000000", Color.Transparent.toHexString())
        assertEquals("#12345678", Color(0x78123456).toHexString())
        assertEquals("#80808080", Color(0.5f, 0.5f, 0.5f, 0.5f).toHexString())
    }
}
