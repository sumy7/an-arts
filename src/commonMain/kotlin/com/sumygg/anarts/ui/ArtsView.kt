package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.viewmodel.ArtsModel

/**
 * 绘制Arts视图部分，上层有TopAppBar展示控制按钮，下方Canvas绘制
 */
@Composable
fun ArtsView(
    artsModel: ArtsModel
) {
    var isPause by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (artsModel.artsType.animated) {
                Checkbox(
                    checked = !isPause,
                    modifier = Modifier.padding(8.dp),
                    onCheckedChange = { isPause = !isPause }
                )
                Text("播放？")
            }
        },
        elevation = 8.dp,
        actions = {
//            IconButton(onClick = {}) {
//                Icon(Icons.Default.ArrowDropDown, contentDescription = "Save")
//            }
        }
    )
    Canvas(
        modifier = Modifier.fillMaxSize().clipToBounds(),
        onDraw = artsModel.arts.onDraw(artsModel.artsConfig)
    )

    LaunchedEffect(artsModel.arts, isPause) {
        if (artsModel.artsType.animated && !isPause) {
            var lastNanos = withFrameNanos { it }
            while (true) {
                withFrameNanos { now ->
                    artsModel.arts.onUpdate((now - lastNanos).coerceAtLeast(0L))
                    lastNanos = now
                }
            }
        }
    }

}
