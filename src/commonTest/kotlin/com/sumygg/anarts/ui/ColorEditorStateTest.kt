package com.sumygg.anarts.ui

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ColorEditorStateTest {
    @Test
    fun restoresTheTriggerWhenPresentAndFallsBackIfItWasDisposed() {
        var fallback = 0
        var trigger = 0
        val state = ColorEditorState { fallback++ }
        state.open(Color.Red, {}, onRestoreFocus = { assertNull(state.request); trigger++; true })
        state.dismiss()
        assertEquals(1, trigger)
        assertEquals(0, fallback)
        state.open(Color.Red, {}, onRestoreFocus = { false })
        state.dismiss()
        assertEquals(1, fallback)
    }

    @Test
    fun liveSelectionUpdatesTheArtworkAndKeepsThePopoverOpen() {
        var artworkColor = Color.Red
        val state = ColorEditorState()
        val anchor = Rect(10f, 20f, 120f, 60f)
        state.open(artworkColor, { artworkColor = it }, anchor)
        val edited = Color(0.2f, 0.4f, 0.8f, 0.25f)
        state.update(edited)
        assertEquals(edited, artworkColor)
        assertEquals(edited, state.request?.color)
        assertEquals(anchor, state.request?.anchor)
    }

    @Test
    fun dismissKeepsTheLiveSelectionAndRestoresFocusOnlyOnce() {
        var dismissed = 0
        var artworkColor = Color.Red
        lateinit var state: ColorEditorState
        state = ColorEditorState { assertNull(state.request); dismissed++ }
        state.open(artworkColor, { artworkColor = it })
        state.update(Color.Blue)
        state.dismiss()
        state.dismiss()
        state.update(Color.Green)
        assertEquals(Color.Blue, artworkColor)
        assertEquals(1, dismissed)
        assertNull(state.request)
    }

    @Test
    fun tabbingAwayDoesNotStealFocusFromTheNextControl() {
        var restored = 0
        val state = ColorEditorState { restored++ }
        state.open(Color.Red, {})
        state.dismiss(restoreFocus = false)
        assertNull(state.request)
        assertEquals(0, restored)
    }

    @Test
    fun reopeningUsesNewColorAndNewCallback() {
        val state = ColorEditorState()
        var first = Color.Red
        var second = Color.Blue
        state.open(first, { first = it })
        state.dismiss()
        state.open(second, { second = it })
        assertEquals(Color.Blue, state.request?.color)
        state.update(Color.Green)
        assertEquals(Color.Red, first)
        assertEquals(Color.Green, second)
        assertNotNull(state.request)
    }
}
