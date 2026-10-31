package com.sumygg.anarts

import androidx.compose.material.Surface
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.sumygg.anarts.ui.ExplorerView
import com.sumygg.anarts.ui.ColorEditorHost
import com.sumygg.anarts.ui.WorkspaceTheme

@Composable
fun App() {
    var darkTheme by remember { mutableStateOf(false) }
    WorkspaceTheme(darkTheme) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
            ColorEditorHost {
                ExplorerView(darkTheme = darkTheme, onToggleTheme = { darkTheme = !darkTheme })
            }
        }
    }
}
