package com.sumygg.anarts.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class WorkspaceLayout { Wide, Medium, Compact }

fun workspaceLayout(widthDp: Float): WorkspaceLayout = when {
    widthDp >= 1100f -> WorkspaceLayout.Wide
    widthDp >= 760f -> WorkspaceLayout.Medium
    else -> WorkspaceLayout.Compact
}

enum class WorkspacePanel { Library, Inspector }

/** Panel preferences stay independent of temporary overlays and focus mode. */
class WorkspaceState {
    var libraryVisible by mutableStateOf(true)
        private set
    var inspectorVisible by mutableStateOf(true)
        private set
    var overlay: WorkspacePanel? by mutableStateOf(null)
        private set
    var focusMode by mutableStateOf(false)
        private set

    fun toggleLibrary(layout: WorkspaceLayout) {
        val wasFocused = focusMode
        focusMode = false
        if (layout == WorkspaceLayout.Compact) {
            overlay = if (overlay == WorkspacePanel.Library) null else WorkspacePanel.Library
        } else {
            overlay = null
            libraryVisible = wasFocused || !libraryVisible
        }
    }

    fun toggleInspector(layout: WorkspaceLayout) {
        val wasFocused = focusMode
        focusMode = false
        if (layout == WorkspaceLayout.Wide) {
            overlay = null
            inspectorVisible = wasFocused || !inspectorVisible
        } else {
            overlay = if (overlay == WorkspacePanel.Inspector) null else WorkspacePanel.Inspector
        }
    }

    fun openSearch(layout: WorkspaceLayout) {
        focusMode = false
        if (layout == WorkspaceLayout.Compact) {
            overlay = WorkspacePanel.Library
        } else {
            overlay = null
            libraryVisible = true
        }
    }

    fun toggleFocusMode() {
        focusMode = !focusMode
        overlay = null
    }

    fun dismissOverlay() {
        overlay = null
    }

    fun onLayoutChanged(layout: WorkspaceLayout) {
        dismissOverlay()
    }

    fun libraryShown(layout: WorkspaceLayout): Boolean =
        !focusMode && layout != WorkspaceLayout.Compact && libraryVisible

    fun inspectorShown(layout: WorkspaceLayout): Boolean =
        !focusMode && layout == WorkspaceLayout.Wide && inspectorVisible
}
