package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import com.sumygg.anarts.viewmodel.ArtsModel

/** Only the canvas owns the frame loop; workspace and theme changes keep playback state. */
@Composable
fun ArtsView(artsModel: ArtsModel, modifier: Modifier = Modifier) {
    Canvas(modifier.clipToBounds(), onDraw = artsModel.arts.onDraw(artsModel.artsConfig))
    LaunchedEffect(artsModel.arts, artsModel.isPlaying) {
        if (artsModel.artsType.animated && artsModel.isPlaying) {
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
