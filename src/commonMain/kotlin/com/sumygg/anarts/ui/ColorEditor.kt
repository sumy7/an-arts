package com.sumygg.anarts.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class ColorEditRequest(color: Color, val onChange: (Color) -> Unit, val anchor: Rect, val onRestoreFocus: () -> Boolean) {
    var color by mutableStateOf(color)
        internal set
}

/** A live color selection stays open until explicitly dismissed. */
class ColorEditorState(private val onDismiss: () -> Unit = {}) {
    var request by mutableStateOf<ColorEditRequest?>(null)
        private set

    fun open(color: Color, onChange: (Color) -> Unit, anchor: Rect = Rect.Zero, onRestoreFocus: () -> Boolean = { false }) {
        request = ColorEditRequest(color, onChange, anchor, onRestoreFocus)
    }

    fun update(color: Color) {
        val current = request ?: return
        current.color = color
        current.onChange(color)
    }

    fun dismiss(restoreFocus: Boolean = true) {
        val current = request ?: return
        request = null
        if (restoreFocus && !current.onRestoreFocus()) onDismiss()
    }
}

val LocalColorEditor = staticCompositionLocalOf<ColorEditorState> { error("ColorEditorHost is required") }

/** Same-owner overlay keeps browser semantics and focus restoration stable. */
@Composable
fun ColorEditorHost(content: @Composable () -> Unit) {
    val contentFocus = remember { FocusRequester() }
    val editor = remember { ColorEditorState { contentFocus.requestFocus() } }
    val popoverFocus = remember { FocusRequester() }
    val palette = LocalWorkspacePalette.current
    val density = LocalDensity.current
    CompositionLocalProvider(LocalColorEditor provides editor) {
        BoxWithConstraints(Modifier.fillMaxSize()) {
            val compact = maxWidth < 600.dp
            Box(Modifier.fillMaxSize().focusRequester(contentFocus).focusRestorer().focusGroup()
                .then(if (editor.request != null && compact) Modifier.clearAndSetSemantics { } else Modifier)) {
                content()
            }
            editor.request?.let { request ->
                var popoverSize by remember(request) { mutableStateOf(IntSize.Zero) }
                val gap = with(density) { 8.dp.toPx() }
                val margin = with(density) { 12.dp.toPx() }
                val viewportWidth = constraints.maxWidth.toFloat()
                val viewportHeight = constraints.maxHeight.toFloat()
                val width = minOf(320.dp, maxWidth - 24.dp)
                val widthPx = with(density) { width.toPx() }
                val x = if (compact) (viewportWidth - widthPx) / 2f else
                    (request.anchor.right - widthPx).coerceIn(margin, (viewportWidth - widthPx - margin).coerceAtLeast(margin))
                val y = if (compact) viewportHeight - popoverSize.height - margin else {
                    when {
                        request.anchor.bottom + gap + popoverSize.height <= viewportHeight - margin -> request.anchor.bottom + gap
                        request.anchor.top - gap - popoverSize.height >= margin -> request.anchor.top - gap - popoverSize.height
                        else -> request.anchor.top.coerceIn(margin, (viewportHeight - popoverSize.height - margin).coerceAtLeast(margin))
                    }
                }
                LaunchedEffect(request) {
                    withFrameNanos { }
                    popoverFocus.requestFocus()
                }
                Box(Modifier.fillMaxSize().pointerInput(editor) {
                    detectTapGestures { editor.dismiss() }
                })
                Surface(
                    Modifier.offset { IntOffset(x.roundToInt(), y.coerceAtLeast(margin).roundToInt()) }
                        .width(width).heightIn(max = maxHeight - 24.dp)
                        .onSizeChanged { popoverSize = it }
                        .focusRequester(popoverFocus)
                        .focusProperties { onExit = {
                            if (editor.request != null) {
                                if (compact) cancelFocusChange() else editor.dismiss(restoreFocus = false)
                            }
                        } }
                        .focusGroup()
                        .onPreviewKeyEvent {
                            if (it.key == Key.Escape && it.type == KeyEventType.KeyDown) {
                                editor.dismiss(); true
                            } else false
                        }
                        .pointerInput(Unit) { awaitPointerEventScope { while (true) awaitPointerEvent() } }
                        .semantics { paneTitle = "颜色选择器" },
                    shape = RoundedCornerShape(8.dp),
                    color = palette.raised,
                    border = BorderStroke(1.dp, palette.border),
                    elevation = 8.dp,
                ) {
                    key(request) { ColorPickerContent(request.color, editor::update, { editor.dismiss() }) }
                }
            }
        }
    }
}
