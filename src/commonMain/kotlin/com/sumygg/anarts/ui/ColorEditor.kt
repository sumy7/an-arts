package com.sumygg.anarts.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

class ColorEditRequest(val color: Color, val onApply: (Color) -> Unit)

/** Keeps modal editing in the workspace's Compose owner on both platforms. */
class ColorEditorState(private val onDismiss: () -> Unit = {}) {
    var request by mutableStateOf<ColorEditRequest?>(null)
        private set

    fun open(color: Color, onApply: (Color) -> Unit) {
        request = ColorEditRequest(color, onApply)
    }

    fun cancel() {
        if (request == null) return
        request = null
        onDismiss()
    }

    fun apply(color: Color) {
        val current = request ?: return
        request = null
        current.onApply(color)
        onDismiss()
    }
}

val LocalColorEditor = staticCompositionLocalOf<ColorEditorState> { error("ColorEditorHost is required") }

@Composable
fun ColorEditorHost(content: @Composable () -> Unit) {
    val contentFocus = remember { FocusRequester() }
    val editor = remember { ColorEditorState { contentFocus.requestFocus() } }
    val dialogFocus = remember { FocusRequester() }
    val palette = LocalWorkspacePalette.current
    CompositionLocalProvider(LocalColorEditor provides editor) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            Box(Modifier.fillMaxSize().focusRequester(contentFocus).focusRestorer().focusGroup()
                .then(if (editor.request != null) Modifier.clearAndSetSemantics { } else Modifier)) {
                content()
            }
            editor.request?.let { request ->
                LaunchedEffect(request) {
                    withFrameNanos { }
                    dialogFocus.requestFocus()
                }
                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)).clickable(
                    interactionSource = remember { MutableInteractionSource() }, indication = null,
                    onClick = editor::cancel,
                ).semantics { contentDescription = "取消颜色编辑" })
                Surface(
                    Modifier.align(Alignment.Center).width(minOf(380.dp, maxWidth - 32.dp))
                        .heightIn(max = maxHeight - 32.dp)
                        .focusRequester(dialogFocus)
                        .focusProperties { onExit = { if (editor.request != null) cancelFocusChange() } }
                        .focusGroup()
                        .onPreviewKeyEvent {
                            if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                                editor.cancel(); true
                            } else false
                        }.semantics { paneTitle = "编辑颜色" },
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colors.surface,
                    border = BorderStroke(1.dp, palette.border),
                    elevation = 0.dp,
                ) {
                    key(request) { ColorEditorContent(request.color, editor::apply, editor::cancel) }
                }
            }
        }
    }
}
