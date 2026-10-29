package com.sumygg.anarts.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

/** The three toolbar icons, shared without the retired material-icons dependency. */
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

    private fun icon(name: String, draw: androidx.compose.ui.graphics.vector.PathBuilder.() -> Unit) =
        ImageVector.Builder(name, 24.dp, 24.dp, 24f, 24f)
            .apply { path(fill = SolidColor(Color.Black), pathBuilder = draw) }
            .build()
}
