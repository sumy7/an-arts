package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.utils.toHexString
import kotlin.math.roundToInt

internal data class PickerHsv(val hue: Float, val saturation: Float, val value: Float) {
    fun color(alpha: Float): Color = Color.hsv(hue.coerceIn(0f, 360f), saturation.coerceIn(0f, 1f), value.coerceIn(0f, 1f), alpha.coerceIn(0f, 1f))
}

internal fun Color.pickerHsv(): PickerHsv {
    val maximum = maxOf(red, green, blue)
    val minimum = minOf(red, green, blue)
    val delta = maximum - minimum
    val hue = when {
        delta == 0f -> 0f
        maximum == red -> 60f * (((green - blue) / delta) % 6f)
        maximum == green -> 60f * ((blue - red) / delta + 2f)
        else -> 60f * ((red - green) / delta + 4f)
    }
    return PickerHsv((hue + 360f) % 360f, if (maximum == 0f) 0f else delta / maximum, maximum)
}

/** Six digits preserve current alpha; eight digits explicitly set RGBA. */
internal fun parsePickerHex(value: String, alpha: Float): Color? {
    val digits = value.trim().removePrefix("#")
    if (digits.length != 6 && digits.length != 8) return null
    if (digits.any { it !in '0'..'9' && it.lowercaseChar() !in 'a'..'f' }) return null
    val channels = digits.chunked(2).map { it.toInt(16) / 255f }
    return Color(channels[0], channels[1], channels[2], if (channels.size == 4) channels[3] else alpha)
}

internal fun parsePickerChannel(value: String): Float? = value.toIntOrNull()?.takeIf { it in 0..255 }?.div(255f)

@Composable
internal fun ColorPickerContent(initialColor: Color, onChange: (Color) -> Unit, onClose: () -> Unit) {
    var color by remember { mutableStateOf(initialColor) }
    var hsv by remember { mutableStateOf(initialColor.pickerHsv()) }
    var hex by remember { mutableStateOf(initialColor.toHexString()) }
    val channels = remember { mutableStateListOf(initialColor.red.channelText(), initialColor.green.channelText(), initialColor.blue.channelText(), initialColor.alpha.channelText()) }
    val palette = LocalWorkspacePalette.current
    fun choose(next: Color, nextHsv: PickerHsv? = null) {
        color = next
        val converted = next.pickerHsv()
        hsv = nextHsv ?: converted.copy(hue = if (converted.saturation == 0f) hsv.hue else converted.hue)
        hex = next.toHexString()
        channels[0] = next.red.channelText(); channels[1] = next.green.channelText()
        channels[2] = next.blue.channelText(); channels[3] = next.alpha.channelText()
        onChange(next)
    }
    Column(Modifier.padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("颜色", style = MaterialTheme.typography.subtitle2)
                Text("调整即生效", style = MaterialTheme.typography.caption, color = palette.muted)
            }
            ShadcnIconButton(ArtIcons.Close, "关闭颜色选择器", onClose)
        }
        Spacer(Modifier.height(12.dp))
        Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SaturationValue(hsv, onChange = { next -> choose(next.color(color.alpha), next) })
            Column {
                PickerLabel("色相", "${hsv.hue.roundToInt()}°")
                ShadcnSlider(
                    value = hsv.hue, onValueChange = { hue ->
                        val next = hsv.copy(hue = hue)
                        choose(next.color(color.alpha), next)
                    }, valueRange = 0f..360f,
                    modifier = Modifier.fillMaxWidth().semantics { contentDescription = "色相" },
                    trackBrush = Brush.horizontalGradient(listOf(Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red)),
                )
            }
            Column {
                PickerLabel("透明度", "${(color.alpha * 100).roundToInt()}%")
                Box {
                    PickerSwatch(Color.Transparent, Modifier.fillMaxWidth().padding(horizontal = 8.dp).height(5.dp).align(Alignment.Center))
                    ShadcnSlider(
                        value = color.alpha, onValueChange = { choose(color.copy(alpha = it), hsv) },
                        modifier = Modifier.fillMaxWidth().semantics { contentDescription = "透明度" },
                        trackBrush = Brush.horizontalGradient(listOf(color.copy(alpha = 0f), color.copy(alpha = 1f))),
                    )
                }
            }
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PickerSwatch(color, Modifier.size(36.dp))
                ShadcnInput(
                    hex, { text ->
                        parsePickerHex(text, color.alpha)?.let { choose(it) }
                        hex = text
                    }, "HEX 颜色", modifier = Modifier.weight(1f),
                    isError = parsePickerHex(hex, color.alpha) == null,
                )
            }
            Text(if (parsePickerHex(hex, color.alpha) == null) "请输入 6 或 8 位十六进制颜色" else "HEX · #RRGGBB / #RRGGBBAA", color = if (parsePickerHex(hex, color.alpha) == null) MaterialTheme.colors.error else palette.muted, style = MaterialTheme.typography.caption)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf("R" to "红色", "G" to "绿色", "B" to "蓝色", "A" to "透明度").forEachIndexed { index, (short, name) ->
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        ShadcnInput(channels[index], { text ->
                            parsePickerChannel(text)?.let { channel ->
                                choose(when (index) {
                                    0 -> color.copy(red = channel)
                                    1 -> color.copy(green = channel)
                                    2 -> color.copy(blue = channel)
                                    else -> color.copy(alpha = channel)
                                })
                            }
                            channels[index] = text
                        }, "$short · $name", isError = parsePickerChannel(channels[index]) == null)
                        Text(short, Modifier.align(Alignment.CenterHorizontally), color = palette.muted, style = MaterialTheme.typography.caption)
                    }
                }
            }
            if (channels.any { parsePickerChannel(it) == null }) {
                Text("RGBA 通道请输入 0–255 的整数", color = MaterialTheme.colors.error, style = MaterialTheme.typography.caption)
            }
            Text("预设颜色", style = MaterialTheme.typography.caption, color = palette.muted)
            val presets = listOf(0xFF18181BL, 0xFF71717AL, 0xFFD4D4D8L, 0xFFFFFFFFL, 0xFFEF4444L, 0xFFF97316L, 0xFFEAB308L, 0xFF22C55EL, 0xFF14B8A6L, 0xFF06B6D4L, 0xFF3B82F6L, 0xFF6366F1L, 0xFF8B5CF6L, 0xFFA855F7L, 0xFFEC4899L, 0xFFF43F5EL)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                presets.chunked(8).forEach { row ->
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        row.forEach { argb ->
                            val swatch = Color(argb).copy(alpha = color.alpha)
                            PickerPreset(swatch, "预设 ${Color(argb).toHexString().take(7)}") { choose(swatch) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PickerLabel(label: String, value: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.caption, fontWeight = FontWeight.Medium)
        Text(value, style = MaterialTheme.typography.caption, color = LocalWorkspacePalette.current.muted)
    }
}

@Composable
private fun SaturationValue(hsv: PickerHsv, onChange: (PickerHsv) -> Unit) {
    var focused by remember { mutableStateOf(false) }
    val focus = remember { FocusRequester() }
    val current by rememberUpdatedState(hsv)
    val change by rememberUpdatedState(onChange)
    val border = if (focused) LocalWorkspacePalette.current.ring else LocalWorkspacePalette.current.border
    fun adjust(saturation: Float = hsv.saturation, value: Float = hsv.value): Boolean {
        onChange(hsv.copy(saturation = saturation.coerceIn(0f, 1f), value = value.coerceIn(0f, 1f)))
        return true
    }
    Canvas(Modifier.fillMaxWidth().height(156.dp).clip(RoundedCornerShape(6.dp))
        .border(if (focused) 2.dp else 1.dp, border, RoundedCornerShape(6.dp))
        .pointerInput(Unit) {
            awaitEachGesture {
                val down = awaitFirstDown()
                focus.requestFocus()
                fun update(position: Offset) {
                    change(current.copy(saturation = (position.x / size.width).coerceIn(0f, 1f), value = (1f - position.y / size.height).coerceIn(0f, 1f)))
                }
                update(down.position); down.consume()
                do {
                    val event = awaitPointerEvent()
                    event.changes.firstOrNull { it.id == down.id }?.let { pointer ->
                        update(pointer.position); pointer.consume()
                    }
                } while (event.changes.any { it.pressed })
            }
        }
        .focusRequester(focus).onFocusChanged { focused = it.isFocused }
        .onPreviewKeyEvent {
            val step = if (it.isShiftPressed) 0.1f else 0.01f
            if (it.type != KeyEventType.KeyDown || it.isAltPressed || it.isMetaPressed || it.isCtrlPressed) false
            else when (it.key) {
                Key.DirectionLeft -> adjust(saturation = hsv.saturation - step)
                Key.DirectionRight -> adjust(saturation = hsv.saturation + step)
                Key.DirectionUp -> adjust(value = hsv.value + step)
                Key.DirectionDown -> adjust(value = hsv.value - step)
                else -> false
            }
        }.focusable().semantics {
            contentDescription = "饱和度与明度色板，左右方向键调整饱和度，上下方向键调整明度"
            stateDescription = "饱和度 ${(hsv.saturation * 100).roundToInt()}%，明度 ${(hsv.value * 100).roundToInt()}%"
            customActions = listOf(
                CustomAccessibilityAction("增加饱和度") { adjust(saturation = hsv.saturation + 0.01f) },
                CustomAccessibilityAction("减少饱和度") { adjust(saturation = hsv.saturation - 0.01f) },
                CustomAccessibilityAction("增加明度") { adjust(value = hsv.value + 0.01f) },
                CustomAccessibilityAction("减少明度") { adjust(value = hsv.value - 0.01f) },
            )
        }) {
        drawRect(Color.hsv(hsv.hue, 1f, 1f))
        drawRect(Brush.horizontalGradient(listOf(Color.White, Color.Transparent)))
        drawRect(Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))
        val radius = 6.dp.toPx()
        val center = Offset((hsv.saturation * size.width).coerceIn(radius, size.width - radius), ((1f - hsv.value) * size.height).coerceIn(radius, size.height - radius))
        drawCircle(Color.Black.copy(alpha = 0.5f), radius, center, style = Stroke(4.dp.toPx()))
        drawCircle(Color.White, radius, center, style = Stroke(2.dp.toPx()))
    }
}

@Composable
private fun PickerPreset(color: Color, label: String, onClick: () -> Unit) {
    val interactions = remember { MutableInteractionSource() }
    val focused by interactions.collectIsFocusedAsState()
    val hovered by interactions.collectIsHoveredAsState()
    val palette = LocalWorkspacePalette.current
    Box(Modifier.size(34.dp).border(if (focused) 2.dp else 1.dp, if (focused) palette.ring else Color.Transparent, RoundedCornerShape(6.dp))
        .clip(RoundedCornerShape(6.dp)).background(if (hovered) palette.selection else Color.Transparent)
        .hoverable(interactions).clickable(interactions, indication = null, role = Role.Button, onClick = onClick)
        .semantics { contentDescription = label }, contentAlignment = Alignment.Center) {
        PickerSwatch(color, Modifier.size(24.dp))
    }
}

@Composable
private fun PickerSwatch(color: Color, modifier: Modifier) {
    val border = LocalWorkspacePalette.current.border
    Canvas(modifier.clip(RoundedCornerShape(4.dp)).border(1.dp, border, RoundedCornerShape(4.dp))) {
        val tile = 5.dp.toPx()
        for (row in 0..(size.height / tile).toInt()) for (column in 0..(size.width / tile).toInt()) {
            drawRect(if ((row + column) % 2 == 0) Color(0xFFF4F4F5) else Color(0xFFA1A1AA), Offset(column * tile, row * tile), Size(tile, tile))
        }
        drawRect(color)
    }
}

private fun Float.channelText(): String = (this * 255).roundToInt().toString()
