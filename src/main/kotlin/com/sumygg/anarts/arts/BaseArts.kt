package com.sumygg.anarts.arts

import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 * Arts基础类，定义了Arts的生命周期函数。
 */
abstract class BaseArts {

    /**
     * Arts被载入时触发，需要在此处初始化内部变量
     */
    abstract fun onInit()

    /**
     * Arts更新的时候触发，用于动画更新，引擎会每隔一段时间定时调用函数。
     * 需要在此处重新计算动画属性值。
     *
     * @param elapsed 距离上次调用经过的事件，单位毫秒ms
     */
    abstract fun onUpdate(elapsed: Long)

    /**
     * Arts在界面的绘制函数，Arts被装载后此函数会被传入Canvas进行界面绘制。
     *
     * @param config 界面属性传入的参数
     * @return 界面绘制函数
     */
    abstract fun onDraw(config: ArtsConfig): DrawScope.() -> Unit

}
