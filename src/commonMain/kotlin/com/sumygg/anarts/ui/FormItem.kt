package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
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
                OutlinedTextField(
                    value = field.state.value,
                    onValueChange = {},
                    enabled = false,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                    textStyle = MaterialTheme.typography.body2,
                )
            }
            is FormField.Input -> {
                FieldLabel(fieldLabel(field.title))
                OutlinedTextField(
                    value = field.state.value,
                    onValueChange = { field.state.value = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = fieldLabel(field.title) },
                    textStyle = MaterialTheme.typography.body2,
                )
            }
            is FormField.Slider -> {
                FieldLabel(fieldLabel(field.title), numberLabel(field.state.value))
                Slider(
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
                Slider(
                    value = field.state.value.toFloat(),
                    onValueChange = { field.state.value = it.toInt() },
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
                field.values.forEach { value ->
                    val selected = field.state.value == value
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (selected) MaterialTheme.colors.primary.copy(alpha = 0.10f) else Color.Transparent)
                            .selectable(selected, role = Role.RadioButton, onClick = { field.state.value = value })
                            .heightIn(min = 44.dp)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        RadioButton(selected = selected, onClick = null, modifier = Modifier.size(20.dp))
                        Text(value, style = MaterialTheme.typography.body2)
                    }
                }
            }
            is FormField.ColorList -> {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text(fieldLabel(field.title), style = MaterialTheme.typography.body2, fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { field.state.add(Color.Black) }, modifier = Modifier.size(44.dp)) {
                        Icon(ArtIcons.Add, contentDescription = "添加颜色", modifier = Modifier.size(18.dp))
                    }
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
                            IconButton(onClick = { field.state.removeAt(index) }, modifier = Modifier.size(44.dp)) {
                                Icon(
                                    ArtIcons.Delete,
                                    contentDescription = "删除第 ${index + 1} 个颜色",
                                    tint = LocalWorkspacePalette.current.muted,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    }
                }
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
    OutlinedButton(
        onClick = { editor.open(color, onColorChange) },
        modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp)
            .semantics { contentDescription = "$label，${color.toHexString()}，编辑颜色" },
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
    ) {
        ColorSwatch(color, Modifier.size(22.dp))
        Spacer(Modifier.width(10.dp))
        Text(color.toHexString(), style = MaterialTheme.typography.caption, fontFamily = FontFamily.Monospace)
        Spacer(Modifier.weight(1f))
        Text("编辑", style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
    }
}

@Composable
internal fun ColorEditorContent(initialColor: Color, onApply: (Color) -> Unit, onCancel: () -> Unit) {
    var draft by remember { mutableStateOf(initialColor) }
    Column(Modifier.padding(24.dp)) {
        Text("编辑颜色", style = MaterialTheme.typography.h6)
        Spacer(Modifier.height(20.dp))
        Column(
            Modifier.weight(1f, fill = false).fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            ColorSwatch(draft, Modifier.fillMaxWidth().height(48.dp))
            Text(draft.toHexString(), style = MaterialTheme.typography.body2, fontFamily = FontFamily.Monospace)
            ColorChannel("R · 红色", draft.red) { draft = draft.copy(red = it) }
            ColorChannel("G · 绿色", draft.green) { draft = draft.copy(green = it) }
            ColorChannel("B · 蓝色", draft.blue) { draft = draft.copy(blue = it) }
            ColorChannel("A · 透明度", draft.alpha) { draft = draft.copy(alpha = it) }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            TextButton(onClick = onCancel) { Text("取消") }
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = { onApply(draft) }) { Text("应用颜色") }
        }
    }
}

@Composable
private fun ColorChannel(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Column {
        FieldLabel(label, (value * 255).roundToInt().toString())
        Slider(
            value = value,
            onValueChange = onValueChange,
            steps = 254,
            modifier = Modifier.fillMaxWidth().semantics { contentDescription = label },
        )
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
