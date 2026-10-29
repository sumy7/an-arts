package com.sumygg.anarts.ui

import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ColorEditorStateTest {
    @Test
    fun cancelLeavesOriginalColorUntouchedAndRestoresFocus() {
        var dismissed = 0
        val state = ColorEditorState { dismissed++ }
        var color = Color.Red
        state.open(color, { color = it })
        state.cancel()
        assertEquals(Color.Red, color)
        assertEquals(1, dismissed)
        assertNull(state.request)
    }

    @Test
    fun applyPreservesAlphaAndClosesBeforeReturningFocus() {
        lateinit var state: ColorEditorState
        state = ColorEditorState { assertNull(state.request) }
        var color = Color.Red
        val edited = Color(0.2f, 0.4f, 0.8f, 0.25f)
        state.open(color, { color = it })
        state.apply(edited)
        assertEquals(edited, color)
        assertNull(state.request)
    }

    @Test
    fun reopeningStartsFromTheNewColorAndDismissesOnlyOnce() {
        var dismissed = 0
        val state = ColorEditorState { dismissed++ }
        state.open(Color.Red, {})
        state.cancel()
        state.cancel()
        state.open(Color.Blue, {})
        assertEquals(Color.Blue, state.request?.color)
        state.cancel()
        assertEquals(2, dismissed)
    }
}
