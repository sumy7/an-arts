package com.sumygg.anarts.arts

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.sumygg.anarts.arts.circleloop.CircleLoop
import com.sumygg.anarts.arts.circleloop.CircleLoopConfig
import com.sumygg.anarts.arts.maze.Maze
import com.sumygg.anarts.arts.maze.MazeConfig
import kotlin.reflect.KClass

/**
 * arts声明，用于定义配对的arts绘制函数和表单配置项
 */
data class Arts(
    val name: String,
    val config: KClass<*>,
    val draw: (ArtsConfig) -> (DrawScope.() -> Unit)
) {

}

/**
 * 所有已经实现的arts定义
 */
val ALL_ARTS = listOf(
    Arts("CircleLoop", CircleLoopConfig::class, ::CircleLoop),
    Arts("Maze", MazeConfig::class, ::Maze)
)
