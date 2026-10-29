package com.sumygg.anarts.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.arts.ArtsConfig

/** Renders the current artwork's live fields as consistently spaced inspector groups. */
@Composable
fun DynamicForm(config: ArtsConfig) {
    Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(24.dp)) {
        if (config.fields.isEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("无需调整参数", style = MaterialTheme.typography.body2)
                Text(
                    "这件作品已准备就绪，直接在画布中欣赏。",
                    style = MaterialTheme.typography.caption,
                    color = LocalWorkspacePalette.current.muted,
                )
            }
        } else {
            config.fields.forEach { field ->
                key(field) { ComposeFormItem(field) }
            }
        }
    }
}
