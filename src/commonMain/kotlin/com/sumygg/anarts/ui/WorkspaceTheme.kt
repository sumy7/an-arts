package com.sumygg.anarts.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class WorkspacePalette(
    val rail: Color,
    val sidebar: Color,
    val stage: Color,
    val raised: Color,
    val border: Color,
    val muted: Color,
    val selection: Color,
    val ring: Color,
)

private val DarkPalette = WorkspacePalette(
    rail = Color(0xFF18181B), sidebar = Color(0xFF18181B),
    stage = Color(0xFF09090B), raised = Color(0xFF09090B),
    border = Color(0xFF3F3F46), muted = Color(0xFFA1A1AA),
    selection = Color(0xFF27272A), ring = Color(0xFF71717A),
)
private val LightPalette = WorkspacePalette(
    rail = Color(0xFFFAFAFA), sidebar = Color(0xFFFAFAFA),
    stage = Color.White, raised = Color.White,
    border = Color(0xFFE4E4E7), muted = Color(0xFF71717A),
    selection = Color(0xFFF4F4F5), ring = Color(0xFFA1A1AA),
)
val LocalWorkspacePalette = staticCompositionLocalOf { DarkPalette }

@Composable
fun WorkspaceTheme(dark: Boolean, content: @Composable () -> Unit) {
    val palette = if (dark) DarkPalette else LightPalette
    val colors = if (dark) darkColors(
        primary = Color(0xFFFAFAFA), secondary = Color(0xFFFAFAFA),
        background = palette.stage, surface = palette.sidebar,
        onPrimary = Color(0xFF18181B), onSecondary = Color(0xFF18181B),
        onBackground = Color(0xFFFAFAFA), onSurface = Color(0xFFFAFAFA),
        error = Color(0xFFFF9B9B),
    ) else lightColors(
        primary = Color(0xFF18181B), secondary = Color(0xFF18181B),
        background = palette.stage, surface = palette.sidebar,
        onPrimary = Color.White, onSecondary = Color.White,
        onBackground = Color(0xFF09090B), onSurface = Color(0xFF09090B),
    )
    CompositionLocalProvider(LocalWorkspacePalette provides palette) {
        MaterialTheme(
            colors = colors,
            typography = Typography(
                defaultFontFamily = FontFamily.SansSerif,
                h6 = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
                subtitle1 = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
                subtitle2 = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),
                body1 = TextStyle(fontSize = 14.sp, lineHeight = 22.sp),
                body2 = TextStyle(fontSize = 13.sp, lineHeight = 20.sp),
                button = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium),
                caption = TextStyle(fontSize = 12.sp, lineHeight = 18.sp),
                overline = TextStyle(fontSize = 11.sp, letterSpacing = 0.5.sp),
            ),
            shapes = Shapes(RoundedCornerShape(6.dp), RoundedCornerShape(8.dp), RoundedCornerShape(12.dp)),
            content = content,
        )
    }
}
