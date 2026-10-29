package com.sumygg.anarts.ui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class WorkspaceStateTest {
    @Test
    fun layoutBreakpointsPreserveCanvasSpace() {
        assertEquals(WorkspaceLayout.Compact, workspaceLayout(390f))
        assertEquals(WorkspaceLayout.Compact, workspaceLayout(759.99f))
        assertEquals(WorkspaceLayout.Medium, workspaceLayout(760f))
        assertEquals(WorkspaceLayout.Medium, workspaceLayout(1099.99f))
        assertEquals(WorkspaceLayout.Wide, workspaceLayout(1100f))
        assertEquals(WorkspaceLayout.Wide, workspaceLayout(1440f))
    }

    @Test
    fun wideSidebarsToggleIndependently() {
        val state = WorkspaceState()
        assertTrue(state.libraryShown(WorkspaceLayout.Wide))
        assertTrue(state.inspectorShown(WorkspaceLayout.Wide))
        state.toggleLibrary(WorkspaceLayout.Wide)
        assertFalse(state.libraryShown(WorkspaceLayout.Wide))
        assertTrue(state.inspectorShown(WorkspaceLayout.Wide))
        state.toggleInspector(WorkspaceLayout.Wide)
        assertFalse(state.inspectorShown(WorkspaceLayout.Wide))
        assertNull(state.overlay)
    }

    @Test
    fun compactPanelsShareOneDismissibleOverlay() {
        val state = WorkspaceState()
        assertFalse(state.libraryShown(WorkspaceLayout.Compact))
        assertFalse(state.inspectorShown(WorkspaceLayout.Compact))
        state.toggleLibrary(WorkspaceLayout.Compact)
        assertEquals(WorkspacePanel.Library, state.overlay)
        state.toggleInspector(WorkspaceLayout.Compact)
        assertEquals(WorkspacePanel.Inspector, state.overlay)
        state.toggleInspector(WorkspaceLayout.Compact)
        assertNull(state.overlay)
        state.toggleLibrary(WorkspaceLayout.Compact)
        state.dismissOverlay()
        assertNull(state.overlay)
    }

    @Test
    fun mediumInspectorOverlaysAndLayoutChangeDismissesIt() {
        val state = WorkspaceState()
        assertTrue(state.libraryShown(WorkspaceLayout.Medium))
        assertFalse(state.inspectorShown(WorkspaceLayout.Medium))
        state.toggleInspector(WorkspaceLayout.Medium)
        assertEquals(WorkspacePanel.Inspector, state.overlay)
        state.onLayoutChanged(WorkspaceLayout.Wide)
        assertNull(state.overlay)
        assertTrue(state.inspectorShown(WorkspaceLayout.Wide))
    }

    @Test
    fun focusModeRestoresSidebarPreferences() {
        val state = WorkspaceState()
        state.toggleInspector(WorkspaceLayout.Wide)
        state.toggleFocusMode()
        assertTrue(state.focusMode)
        assertFalse(state.libraryShown(WorkspaceLayout.Wide))
        assertFalse(state.inspectorShown(WorkspaceLayout.Wide))
        state.toggleFocusMode()
        assertTrue(state.libraryShown(WorkspaceLayout.Wide))
        assertFalse(state.inspectorShown(WorkspaceLayout.Wide))
    }

    @Test
    fun searchOpensTheAppropriatePanelAndExitsFocusMode() {
        val state = WorkspaceState()
        state.toggleLibrary(WorkspaceLayout.Wide)
        state.toggleFocusMode()
        state.openSearch(WorkspaceLayout.Wide)
        assertFalse(state.focusMode)
        assertTrue(state.libraryShown(WorkspaceLayout.Wide))
        state.toggleInspector(WorkspaceLayout.Compact)
        state.openSearch(WorkspaceLayout.Compact)
        assertEquals(WorkspacePanel.Library, state.overlay)
        state.openSearch(WorkspaceLayout.Medium)
        assertNull(state.overlay)
        assertTrue(state.libraryShown(WorkspaceLayout.Medium))
    }

    @Test
    fun openingAPanelExitsFocusModeWithoutLosingTheOtherPreference() {
        val state = WorkspaceState()
        state.toggleFocusMode()
        state.toggleInspector(WorkspaceLayout.Compact)
        assertFalse(state.focusMode)
        assertEquals(WorkspacePanel.Inspector, state.overlay)
        assertTrue(state.libraryVisible)
        assertTrue(state.inspectorVisible)
        state.toggleFocusMode()
        assertNull(state.overlay)
    }

    @Test
    fun libraryActionExitsFocusModeAndOpensThePanelAtWideAndMediumWidths() {
        for (layout in listOf(WorkspaceLayout.Wide, WorkspaceLayout.Medium)) {
            val state = WorkspaceState()
            state.toggleInspector(WorkspaceLayout.Wide)
            state.toggleFocusMode()
            state.toggleLibrary(layout)
            assertFalse(state.focusMode)
            assertTrue(state.libraryShown(layout))
            assertFalse(state.inspectorVisible)
            assertNull(state.overlay)
        }
    }

    @Test
    fun wideInspectorActionExitsFocusModeAndOpensThePanel() {
        val state = WorkspaceState()
        state.toggleLibrary(WorkspaceLayout.Wide)
        state.toggleFocusMode()
        state.toggleInspector(WorkspaceLayout.Wide)
        assertFalse(state.focusMode)
        assertTrue(state.inspectorShown(WorkspaceLayout.Wide))
        assertFalse(state.libraryVisible)
        assertNull(state.overlay)
    }

    @Test
    fun mediumInspectorActionExitsFocusModeWithoutChangingSidebarPreferences() {
        val state = WorkspaceState()
        state.toggleLibrary(WorkspaceLayout.Medium)
        state.toggleFocusMode()
        state.toggleInspector(WorkspaceLayout.Medium)
        assertFalse(state.focusMode)
        assertEquals(WorkspacePanel.Inspector, state.overlay)
        assertFalse(state.libraryVisible)
        assertTrue(state.inspectorVisible)
    }
}
