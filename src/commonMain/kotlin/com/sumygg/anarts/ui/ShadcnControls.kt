package com.sumygg.anarts.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class ShadcnButtonVariant { Default, Outline, Ghost, Secondary }

/** Shared primitives use neutral tokens and explicit focus rings, without Material ripples. */
@Composable
fun ShadcnButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ShadcnButtonVariant = ShadcnButtonVariant.Default,
    enabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
    content: @Composable RowScope.() -> Unit,
) {
    val palette = LocalWorkspacePalette.current
    val colors = MaterialTheme.colors
    val interactions = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }
    val hovered by interactions.collectIsHoveredAsState()
    val pressed by interactions.collectIsPressedAsState()
    var focused by remember { mutableStateOf(false) }
    val background = when (variant) {
        ShadcnButtonVariant.Default -> colors.primary.copy(alpha = if (pressed) .75f else if (hovered) .9f else 1f)
        ShadcnButtonVariant.Secondary -> palette.selection
        else -> if (hovered || pressed) palette.selection else if (variant == ShadcnButtonVariant.Outline) palette.raised else Color.Transparent
    }
    val foreground = if (variant == ShadcnButtonVariant.Default) colors.onPrimary else colors.onSurface
    val shape = RoundedCornerShape(6.dp)
    Row(
        modifier.heightIn(min = 36.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { focused = it.isFocused }
            .border(if (focused) 2.dp else 1.dp, if (focused) palette.ring else if (variant == ShadcnButtonVariant.Outline) palette.border else Color.Transparent, shape)
            .clip(shape).background(background.copy(alpha = if (enabled) background.alpha else background.alpha * .5f))
            .hoverable(interactions, enabled)
            .clickable(interactions, indication = null, enabled = enabled, role = Role.Button, onClick = {
                focusRequester.requestFocus()
                onClick()
            })
            .padding(contentPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        CompositionLocalProvider(LocalContentColor provides foreground.copy(alpha = if (enabled) 1f else .5f)) {
            ProvideTextStyle(MaterialTheme.typography.button) { content() }
        }
    }
}

@Composable
fun ShadcnIconButton(icon: ImageVector, label: String, onClick: () -> Unit, active: Boolean = false, modifier: Modifier = Modifier) {
    ShadcnButton(onClick, modifier.size(36.dp).semantics { contentDescription = label },
        if (active) ShadcnButtonVariant.Secondary else ShadcnButtonVariant.Ghost,
        contentPadding = PaddingValues(0.dp)) {
        Icon(icon, null, Modifier.size(16.dp))
    }
}

@Composable
fun ShadcnInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    isError: Boolean = false,
) {
    val palette = LocalWorkspacePalette.current
    var focused by remember { mutableStateOf(false) }
    val foreground = MaterialTheme.colors.onSurface
    BasicTextField(
        value, onValueChange, enabled = enabled, singleLine = true,
        textStyle = MaterialTheme.typography.body2.copy(color = foreground.copy(alpha = if (enabled) 1f else .5f)),
        cursorBrush = SolidColor(foreground),
        modifier = modifier.heightIn(min = 36.dp).semantics { contentDescription = label; if (isError) error("输入无效") }
            .onFocusChanged { focused = it.isFocused },
        decorationBox = { input ->
            Row(Modifier.fillMaxWidth().heightIn(min = 36.dp)
                .border(if (focused) 2.dp else 1.dp, if (isError) MaterialTheme.colors.error else if (focused) palette.ring else palette.border, RoundedCornerShape(6.dp))
                .background(palette.raised, RoundedCornerShape(6.dp)).padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                leading?.invoke()
                Box(Modifier.weight(1f)) {
                    if (value.isEmpty()) Text(placeholder, style = MaterialTheme.typography.body2, color = palette.muted)
                    input()
                }
                trailing?.invoke()
            }
        },
    )
}

internal fun snapSliderValue(value: Float, range: ClosedFloatingPointRange<Float>, steps: Int): Float {
    val clamped = value.coerceIn(range)
    if (steps <= 0 || range.endInclusive <= range.start) return clamped
    val intervals = steps + 1
    return range.start + ((clamped - range.start) / (range.endInclusive - range.start) * intervals).roundToInt() *
        (range.endInclusive - range.start) / intervals
}

@Composable
fun ShadcnSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    modifier: Modifier = Modifier,
    trackBrush: Brush? = null,
) {
    require(valueRange.endInclusive > valueRange.start && steps >= 0)
    val palette = LocalWorkspacePalette.current
    val colors = MaterialTheme.colors
    val focus = remember { FocusRequester() }
    var focused by remember { mutableStateOf(false) }
    val currentChange by rememberUpdatedState(onValueChange)
    val currentValue by rememberUpdatedState(value)
    fun change(next: Float): Boolean {
        val snapped = snapSliderValue(next, valueRange, steps)
        if (snapped == currentValue) return false
        currentChange(snapped)
        return true
    }
    Canvas(modifier.fillMaxWidth().height(32.dp).focusRequester(focus)
        .onFocusChanged { focused = it.isFocused }
        .semantics {
            progressBarRangeInfo = ProgressBarRangeInfo(value.coerceIn(valueRange), valueRange, steps)
            setProgress { change(it) }
        }
        .onPreviewKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown || event.isCtrlPressed || event.isMetaPressed || event.isAltPressed) false
            else {
                val increment = (valueRange.endInclusive - valueRange.start) / if (steps > 0) (steps + 1) else 100
                when (event.key) {
                    Key.DirectionRight, Key.DirectionUp -> { change(currentValue + increment); true }
                    Key.DirectionLeft, Key.DirectionDown -> { change(currentValue - increment); true }
                    Key.PageUp -> { change(currentValue + increment * 10); true }
                    Key.PageDown -> { change(currentValue - increment * 10); true }
                    Key.MoveHome -> { change(valueRange.start); true }
                    Key.MoveEnd -> { change(valueRange.endInclusive); true }
                    else -> false
                }
            }
        }.focusable()
        .pointerInput(valueRange, steps) {
            detectTapGestures { point ->
                focus.requestFocus()
                val fraction = ((point.x - 8.dp.toPx()) / (size.width - 16.dp.toPx())).coerceIn(0f, 1f)
                change(valueRange.start + fraction * (valueRange.endInclusive - valueRange.start))
            }
        }
        .pointerInput(valueRange, steps) {
            detectDragGestures(onDragStart = { point ->
                focus.requestFocus()
                change(valueRange.start + ((point.x - 8.dp.toPx()) / (size.width - 16.dp.toPx())).coerceIn(0f, 1f) * (valueRange.endInclusive - valueRange.start))
            }) { pointer, _ ->
                pointer.consume()
                change(valueRange.start + ((pointer.position.x - 8.dp.toPx()) / (size.width - 16.dp.toPx())).coerceIn(0f, 1f) * (valueRange.endInclusive - valueRange.start))
            }
        }) {
        val start = 8.dp.toPx()
        val end = size.width - start
        val center = size.height / 2
        val x = start + (value.coerceIn(valueRange) - valueRange.start) / (valueRange.endInclusive - valueRange.start) * (end - start)
        val stroke = 5.dp.toPx()
        if (trackBrush != null) drawLine(trackBrush, Offset(start, center), Offset(end, center), strokeWidth = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        else {
            drawLine(palette.selection, Offset(start, center), Offset(end, center), strokeWidth = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            drawLine(colors.primary, Offset(start, center), Offset(x, center), strokeWidth = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round)
        }
        if (focused) drawCircle(palette.ring.copy(alpha = .45f), 11.dp.toPx(), Offset(x, center), style = Stroke(3.dp.toPx()))
        drawCircle(palette.raised, 7.dp.toPx(), Offset(x, center))
        drawCircle(colors.primary, 7.dp.toPx(), Offset(x, center), style = Stroke(1.5.dp.toPx()))
    }
}
