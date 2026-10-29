package com.sumygg.anarts.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** Small shared workspace icons, drawn with the existing vector infrastructure. */
object ArtIcons {
    val List: ImageVector = icon("List") {
        for (y in listOf(5f, 11f, 17f)) {
            moveTo(3f, y); horizontalLineTo(5f); verticalLineTo(y + 2f)
            horizontalLineTo(3f); close()
            moveTo(7f, y); horizontalLineTo(21f); verticalLineTo(y + 2f)
            horizontalLineTo(7f); close()
        }
    }
    val Add: ImageVector = icon("Add") {
        moveTo(19f, 13f); horizontalLineTo(13f); verticalLineTo(19f)
        horizontalLineTo(11f); verticalLineTo(13f); horizontalLineTo(5f)
        verticalLineTo(11f); horizontalLineTo(11f); verticalLineTo(5f)
        horizontalLineTo(13f); verticalLineTo(11f); horizontalLineTo(19f); close()
    }
    val Delete: ImageVector = icon("Delete") {
        moveTo(6f, 19f); verticalLineTo(7f); horizontalLineTo(18f)
        verticalLineTo(19f); horizontalLineTo(6f); close()
        moveTo(19f, 4f); horizontalLineTo(15.5f); lineTo(14.5f, 3f)
        horizontalLineTo(9.5f); lineTo(8.5f, 4f); horizontalLineTo(5f)
        verticalLineTo(6f); horizontalLineTo(19f); close()
    }

    val Search = icon("Search") {
        moveTo(10f, 3f); arcTo(7f, 7f, 0f, true, false, 10f, 17f)
        arcTo(7f, 7f, 0f, true, false, 10f, 3f); close()
        moveTo(10f, 5f); arcTo(5f, 5f, 0f, true, true, 10f, 15f)
        arcTo(5f, 5f, 0f, true, true, 10f, 5f); close()
        moveTo(15f, 14f); lineTo(21f, 20f); lineTo(20f, 21f); lineTo(14f, 15f); close()
    }
    val Close = icon("Close") {
        moveTo(6f, 5f); lineTo(12f, 11f); lineTo(18f, 5f); lineTo(19f, 6f)
        lineTo(13f, 12f); lineTo(19f, 18f); lineTo(18f, 19f); lineTo(12f, 13f)
        lineTo(6f, 19f); lineTo(5f, 18f); lineTo(11f, 12f); lineTo(5f, 6f); close()
    }
    val Play = icon("Play") {
        moveTo(8f, 5f); lineTo(19f, 12f); lineTo(8f, 19f); close()
    }
    val Pause = icon("Pause") {
        moveTo(6f, 5f); horizontalLineTo(10f); verticalLineTo(19f); horizontalLineTo(6f); close()
        moveTo(14f, 5f); horizontalLineTo(18f); verticalLineTo(19f); horizontalLineTo(14f); close()
    }
    val Reset = icon("Reset") {
        moveTo(12f, 4f); arcTo(8f, 8f, 0f, true, true, 4f, 12f)
        horizontalLineTo(6f); arcTo(6f, 6f, 0f, true, false, 12f, 6f)
        verticalLineTo(10f); lineTo(6f, 5f); lineTo(12f, 0f); close()
    }
    val Inspector = icon("Inspector") {
        moveTo(3f, 4f); horizontalLineTo(21f); verticalLineTo(20f); horizontalLineTo(3f); close()
        moveTo(5f, 6f); verticalLineTo(18f); horizontalLineTo(14f); verticalLineTo(6f); close()
        moveTo(16f, 6f); verticalLineTo(18f); horizontalLineTo(19f); verticalLineTo(6f); close()
    }
    val Focus = icon("Focus") {
        moveTo(3f, 3f); horizontalLineTo(10f); verticalLineTo(5f); horizontalLineTo(5f)
        verticalLineTo(10f); horizontalLineTo(3f); close()
        moveTo(14f, 3f); horizontalLineTo(21f); verticalLineTo(10f); horizontalLineTo(19f)
        verticalLineTo(5f); horizontalLineTo(14f); close()
        moveTo(3f, 14f); horizontalLineTo(5f); verticalLineTo(19f); horizontalLineTo(10f)
        verticalLineTo(21f); horizontalLineTo(3f); close()
        moveTo(19f, 14f); horizontalLineTo(21f); verticalLineTo(21f); horizontalLineTo(14f)
        verticalLineTo(19f); horizontalLineTo(19f); close()
    }
    val Appearance = icon("Appearance") {
        moveTo(12f, 2f); arcTo(10f, 10f, 0f, true, false, 12f, 22f)
        arcTo(10f, 10f, 0f, true, false, 12f, 2f); close()
        moveTo(12f, 4f); verticalLineTo(20f); arcTo(8f, 8f, 0f, true, false, 12f, 4f); close()
    }
    val Artwork = icon("Artwork") {
        moveTo(12f, 2f); lineTo(22f, 12f); lineTo(12f, 22f); lineTo(2f, 12f); close()
        moveTo(12f, 5f); lineTo(5f, 12f); lineTo(12f, 19f); lineTo(19f, 12f); close()
        moveTo(12f, 8f); lineTo(16f, 12f); lineTo(12f, 16f); lineTo(8f, 12f); close()
    }

    private fun icon(name: String, draw: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit) =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f)
            .apply { path(fill = SolidColor(Color.Black), pathBuilder = draw) }
            .build()
}
