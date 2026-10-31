package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.geometry.Rect
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.ui.formitem.FormField
import com.sumygg.anarts.utils.toHexString
import kotlin.math.roundToInt

/** Field groups share the inspector's spacing, labels and neutral controls. */
@Composable
fun ComposeFormItem(field: FormField) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        when (field) {
            is FormField.Text -> {
                FieldLabel(fieldLabel(field.title))
                ShadcnInput(
                    value = field.state.value,
                    onValueChange = {},
                    enabled = false,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                    label = fieldLabel(field.title),
                )
            }
            is FormField.Input -> {
                FieldLabel(fieldLabel(field.title))
                ShadcnInput(
                    value = field.state.value,
                    onValueChange = { field.state.value = it },
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                    label = fieldLabel(field.title),
                )
            }
            is FormField.Slider -> {
                FieldLabel(fieldLabel(field.title), numberLabel(field.state.value))
                ShadcnSlider(
                    value = field.state.value,
                    onValueChange = { field.state.value = it },
                    valueRange = field.min..field.max,
                    steps = field.steps,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                )
                RangeLabels(numberLabel(field.min), numberLabel(field.max))
            }
            is FormField.IntSlider -> {
                FieldLabel(fieldLabel(field.title), field.state.value.toString())
                ShadcnSlider(
                    value = field.state.value.toFloat(),
                    onValueChange = { field.state.value = it.roundToInt() },
                    valueRange = field.min..field.max,
                    steps = field.steps,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                )
                RangeLabels(numberLabel(field.min), numberLabel(field.max))
            }
            is FormField.ColorChoose -> {
                FieldLabel(fieldLabel(field.title))
                ColorButton(fieldLabel(field.title), field.state.value) { field.state.value = it }
            }
            is FormField.Radio -> {
                FieldLabel(fieldLabel(field.title))
                ShadcnRadioGroup(field.values, field.state.value) { field.state.value = it }
            }
            is FormField.ColorList -> {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(fieldLabel(field.title), style = MaterialTheme.typography.body2, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    ShadcnIconButton(ArtIcons.Add, "添加颜色", { field.state.add(Color.Black) })
                }
                field.state.forEachIndexed { index, color ->
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(Modifier.weight(1f)) {
                            ColorButton("第 ${index + 1} 个颜色", color) { field.state[index] = it }
                        }
                        if (field.canEmpty || field.state.size > 1) {
                            ShadcnIconButton(ArtIcons.Delete, "删除第 ${index + 1} 个颜色", { field.state.removeAt(index) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShadcnRadioGroup(values: List<String>, selected: String, onSelect: (String) -> Unit) {
    val requesters = remember(values) { values.map { FocusRequester() } }
    val palette = LocalWorkspacePalette.current
    var pendingFocus by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(pendingFocus, selected) {
        pendingFocus?.let { index ->
            withFrameNanos { }
            requesters[index].requestFocus()
            pendingFocus = null
        }
    }
    Column(Modifier.selectableGroup(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        values.forEachIndexed { index, value ->
            val interactions = remember { MutableInteractionSource() }
            val hovered by interactions.collectIsHoveredAsState()
            var focused by remember { mutableStateOf(false) }
            val active = value == selected
            Row(Modifier.fillMaxWidth().heightIn(min = 36.dp)
                .focusRequester(requesters[index]).focusProperties { canFocus = active }
                .onFocusChanged { focused = it.isFocused }
                .onPreviewKeyEvent { event ->
                    if (event.type != KeyEventType.KeyDown || event.isAltPressed || event.isCtrlPressed || event.isMetaPressed) false
                    else {
                        val target = when (event.key) {
                            Key.DirectionDown, Key.DirectionRight -> (index + 1) % values.size
                            Key.DirectionUp, Key.DirectionLeft -> (index - 1 + values.size) % values.size
                            Key.MoveHome -> 0
                            Key.MoveEnd -> values.lastIndex
                            else -> -1
                        }
                        if (target < 0) false else { onSelect(values[target]); pendingFocus = target; true }
                    }
                }
                .border(if (focused) 2.dp else 1.dp, if (focused) palette.ring else Color.Transparent, RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp)).background(if (hovered) palette.selection else Color.Transparent)
                .selectable(active, interactions, indication = null, role = Role.RadioButton, onClick = { onSelect(value) })
                .padding(horizontal = 6.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                val foreground = MaterialTheme.colors.onSurface
                Canvas(Modifier.size(16.dp)) {
                    drawCircle(foreground, radius = 7.dp.toPx(), style = androidx.compose.ui.graphics.drawscope.Stroke(1.dp.toPx()))
                    if (active) drawCircle(foreground, radius = 4.dp.toPx())
                }
                Text(value, style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
private fun FieldLabel(label: String, value: String? = null) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.body2, fontWeight = FontWeight.Medium)
        if (value != null) {
            Spacer(Modifier.weight(1f))
            Text(value, style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
        }
    }
}

@Composable
private fun RangeLabels(min: String, max: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(min, style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
        Text(max, style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
    }
}

@Composable
private fun ColorButton(label: String, color: Color, onColorChange: (Color) -> Unit) {
    val editor = LocalColorEditor.current
    var anchor by remember { mutableStateOf(Rect.Zero) }
    val triggerFocus = remember { FocusRequester() }
    var attached by remember { mutableStateOf(true) }
    DisposableEffect(Unit) { onDispose { attached = false } }
    ShadcnButton(
        onClick = { editor.open(color, onColorChange, anchor, onRestoreFocus = {
            if (attached) triggerFocus.requestFocus() else false
        }) },
        modifier = Modifier.fillMaxWidth().heightIn(min = 36.dp)
            .focusRequester(triggerFocus)
            .onGloballyPositioned { anchor = it.boundsInRoot() }
            .semantics { contentDescription = "$label，${color.toHexString()}，编辑颜色" },
        variant = ShadcnButtonVariant.Outline,
    ) {
        ColorSwatch(color, Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Text(color.toHexString(), style = MaterialTheme.typography.caption, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.weight(1f))
        Text("选择", style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
    }
}

/** A checkerboard makes alpha visible while keeping text outside the colored area. */
@Composable
private fun ColorSwatch(color: Color, modifier: Modifier) {
    val border = MaterialTheme.colors.onSurface.copy(alpha = 0.16f)
    Canvas(modifier.clip(RoundedCornerShape(4.dp)).border(1.dp, border, RoundedCornerShape(4.dp))) {
        val tile = 6.dp.toPx()
        for (row in 0..(size.height / tile).toInt()) {
            for (column in 0..(size.width / tile).toInt()) {
                drawRect(
                    if ((row + column) % 2 == 0) Color(0xFFE0E0E0) else Color(0xFFAAAAAA),
                    topLeft = Offset(column * tile, row * tile),
                    size = Size(tile, tile),
                )
            }
        }
        drawRect(color)
    }
}

private fun numberLabel(value: Float): String = ((value * 100).roundToInt() / 100f).toString().removeSuffix(".0")

private fun fieldLabel(title: String): String = when (title) {
    "background" -> "背景颜色"
    "foreground" -> "线条颜色"
    "colors" -> "调色板"
    "radius" -> "半径"
    "number" -> "数值"
    "degrees" -> "分支角度"
    "text" -> "生成文本"
    "hashType" -> "哈希算法"
    else -> title
}
