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
)

private val DarkPalette = WorkspacePalette(
    rail = Color(0xFF151518), sidebar = Color(0xFF19191C),
    stage = Color(0xFF1E1E20), raised = Color(0xFF252528),
    border = Color(0xFF333338), muted = Color(0xFFA6A6B2), selection = Color(0xFF332D45),
)
private val LightPalette = WorkspacePalette(
    rail = Color(0xFFECECEF), sidebar = Color(0xFFF4F4F6),
    stage = Color(0xFFFAFAFB), raised = Color.White,
    border = Color(0xFFDDDDE3), muted = Color(0xFF656571), selection = Color(0xFFE9E2FA),
)
val LocalWorkspacePalette = staticCompositionLocalOf { DarkPalette }

@Composable
fun WorkspaceTheme(dark: Boolean, content: @Composable () -> Unit) {
    val palette = if (dark) DarkPalette else LightPalette
    val colors = if (dark) darkColors(
        primary = Color(0xFFB5A2F3), secondary = Color(0xFFB5A2F3),
        background = palette.stage, surface = palette.sidebar,
        onPrimary = Color(0xFF211A32), onSecondary = Color(0xFF211A32),
        onBackground = Color(0xFFECECF0), onSurface = Color(0xFFECECF0),
        error = Color(0xFFFF9B9B),
    ) else lightColors(
        primary = Color(0xFF7051B5), secondary = Color(0xFF7051B5),
        background = palette.stage, surface = palette.sidebar,
        onPrimary = Color.White, onSecondary = Color.White,
        onBackground = Color(0xFF292930), onSurface = Color(0xFF292930),
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
            shapes = Shapes(RoundedCornerShape(6.dp), RoundedCornerShape(10.dp), RoundedCornerShape(12.dp)),
            content = content,
        )
    }
}
