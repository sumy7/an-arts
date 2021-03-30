package com.sumygg.anarts.arts

import com.sumygg.anarts.arts.circleloop.CircleLoop
import com.sumygg.anarts.arts.circleloop.CircleLoopConfig
import com.sumygg.anarts.arts.maze.Maze
import com.sumygg.anarts.arts.maze.MazeConfig
import kotlin.reflect.KClass

/**
 * arts声明，用于定义配对的arts绘制函数和表单配置项
 */
enum class Arts(
    val artsName: String,
    val artsConfig: KClass<out ArtsConfig>,
    val arts: KClass<out BaseArts>
) {
    CIRCLE_LOOP("CircleLoop", CircleLoopConfig::class, CircleLoop::class),
    MAZE("Maze", MazeConfig::class, Maze::class),
    ;

    companion object {
        /**
         * 按照Arts名称字母顺序排序后的所有值
         */
        val sortedValues: List<Arts> by lazy {
            values().sortedBy { it.artsName }
        }
    }
}
