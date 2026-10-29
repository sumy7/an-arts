package com.sumygg.anarts.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.ui.formitem.FormField
import com.sumygg.anarts.utils.reverse
import com.sumygg.anarts.utils.toHexString
import kotlin.math.roundToInt

/** Renders a typed form field without reflection or unchecked state casts. */
@Composable
fun ComposeFormItem(field: FormField) {
    when (field) {
        is FormField.Text -> {
            Text(field.title)
            TextField(value = field.state.value, enabled = false, onValueChange = {})
        }
        is FormField.Input -> {
            Text(field.title)
            TextField(value = field.state.value, onValueChange = { field.state.value = it })
        }
        is FormField.Slider -> {
            Text("${field.title} = ${field.state.value}")
            Slider(
                value = field.state.value,
                onValueChange = { field.state.value = it },
                valueRange = field.min..field.max,
                steps = field.steps,
            )
        }
        is FormField.IntSlider -> {
            Text("${field.title} = ${field.state.value}")
            Slider(
                value = field.state.value.toFloat(),
                onValueChange = { field.state.value = it.toInt() },
                valueRange = field.min..field.max,
                steps = field.steps,
            )
        }
        is FormField.ColorChoose -> {
            Text(field.title)
            ColorButton(field.state.value) { field.state.value = it }
        }
        is FormField.Radio -> {
            Text(field.title)
            field.values.forEach { value ->
                Row {
                    RadioButton(
                        selected = field.state.value == value,
                        onClick = { field.state.value = value },
                    )
                    Text(value)
                }
            }
        }
        is FormField.ColorList -> {
            Row {
                Text(field.title)
                IconButton(onClick = { field.state.add(Color.Black) }) {
                    Icon(ArtIcons.Add, contentDescription = "添加颜色")
                }
            }
            field.state.forEachIndexed { index, color ->
                Row {
                    ColorButton(color) { field.state[index] = it }
                    Spacer(Modifier.width(8.dp))
                    if (field.canEmpty || field.state.size > 1) {
                        IconButton(onClick = { field.state.removeAt(index) }) {
                            Icon(ArtIcons.Delete, contentDescription = "删除颜色")
                        }
                    }
                }
                Divider()
            }
        }
    }
}

/** The same RGBA picker is available on desktop and in the browser. */
@Composable
private fun ColorButton(color: Color, onColorChange: (Color) -> Unit) {
    var isOpen by remember { mutableStateOf(false) }
    Button(
        onClick = { isOpen = true },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            contentColor = color.reverse(),
        ),
    ) {
        Text(color.toHexString())
    }
    if (isOpen) {
        var draft by remember { mutableStateOf(color) }
        AlertDialog(
            onDismissRequest = { isOpen = false },
            title = { Text("请选择颜色") },
            text = {
                Column {
                    Box(Modifier.fillMaxWidth().height(40.dp).background(draft))
                    Text(draft.toHexString())
                    ColorChannel("R", draft.red) { draft = draft.copy(red = it) }
                    ColorChannel("G", draft.green) { draft = draft.copy(green = it) }
                    ColorChannel("B", draft.blue) { draft = draft.copy(blue = it) }
                    ColorChannel("A", draft.alpha) { draft = draft.copy(alpha = it) }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onColorChange(draft)
                    isOpen = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { isOpen = false }) { Text("取消") }
            },
        )
    }
}

@Composable
private fun ColorChannel(label: String, value: Float, onValueChange: (Float) -> Unit) {
    Text("$label = ${(value * 255).roundToInt()}")
    Slider(value = value, onValueChange = onValueChange, steps = 254)
}
