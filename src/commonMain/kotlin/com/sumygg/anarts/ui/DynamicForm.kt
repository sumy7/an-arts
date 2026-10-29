package com.sumygg.anarts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.sumygg.anarts.arts.ArtsConfig

/** Renders portable, explicitly declared fields without JVM reflection. */
@Composable
fun DynamicForm(config: ArtsConfig) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (config.fields.isEmpty()) {
            Text("此Art无配置项")
        } else {
            config.fields.forEach { field ->
                key(field) { ComposeFormItem(field) }
            }
        }
    }
}
