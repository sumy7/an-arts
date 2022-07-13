package com.sumygg.anarts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.sumygg.anarts.ui.ExplorerView

/**
 * 程序入口
 */
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "an-arts Explorer",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        MenuBar {
            Menu("Actions") { Item("Exit", onClick = ::exitApplication) }
        }
        MaterialTheme {
            ExplorerView()
        }
    }
}
