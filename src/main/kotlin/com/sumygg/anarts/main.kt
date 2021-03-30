package com.sumygg.anarts

import androidx.compose.desktop.AppManager
import androidx.compose.desktop.DesktopTheme
import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Menu
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.MenuItem
import com.sumygg.anarts.ui.ExplorerView

/**
 * 程序入口
 */
fun main() = Window(
    title = "an-arts Explorer",
    size = IntSize(800, 600),
    menuBar = MenuBar(
        Menu("Actions", MenuItem("Exit", onClick = {
            AppManager.exit()
        }))
    )
) {
    MaterialTheme {
        DesktopTheme {
            ExplorerView()
        }
    }
}
