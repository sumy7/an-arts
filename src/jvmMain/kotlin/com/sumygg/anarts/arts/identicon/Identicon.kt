package com.sumygg.anarts.arts.identicon

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts
import com.sumygg.anarts.utils.hslToRgb
import com.sumygg.anarts.utils.toHexString
import java.security.MessageDigest
import kotlin.math.min

/**
 * 绘制 Identicon
 */
class Identicon : BaseArts("Identicon") {

    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is IdenticonConfig) {
            throw IllegalArgumentException("${this.name} must use ${IdenticonConfig::class.simpleName}")
        }

        return {
            // 计算hash值
            val md = MessageDigest.getInstance(config.hashType.value)
            val byteHexString = md.digest(config.text.value.toByteArray()).toHexString()


            // 最后7位计算成颜色值，用70%饱和度和50%亮度进行调和
            val hue = 1.0f * java.lang.Long.parseLong(byteHexString.substring(byteHexString.length - 7), 16) / 0xfffffff
            val (r, g, b) = hslToRgb(hue, 0.7f, 0.5f)
            val foreground = Color(r, g, b)
            val background = Color.White
            val cell = min(this.size.width, this.size.height) / 5

            translate((this.size.width - cell * 5) / 2, (this.size.height - cell * 5) / 2) {
                // 前15位的奇偶控制像素展示
                // 先绘制中间，然后镜像绘制外侧
                for (i in 0 until 15) {
                    val color =
                        if (Integer.parseInt(byteHexString[i].toString(), 16) % 2 == 1) foreground else background
                    when {
                        i < 5 -> {
                            drawRect(
                                color = color,
                                topLeft = Offset(2.0f * cell, i * cell),
                                size = Size(cell, cell)
                            )
                        }
                        i < 10 -> {
                            drawRect(
                                color = color,
                                topLeft = Offset(1.0f * cell, (i - 5) * cell),
                                size = Size(cell, cell)
                            )
                            drawRect(
                                color = color,
                                topLeft = Offset(3.0f * cell, (i - 5) * cell),
                                size = Size(cell, cell)
                            )
                        }
                        i < 15 -> {
                            drawRect(
                                color = color,
                                topLeft = Offset(0f * cell, (i - 10) * cell),
                                size = Size(cell, cell)
                            )
                            drawRect(
                                color = color,
                                topLeft = Offset(4.0f * cell, (i - 10) * cell),
                                size = Size(cell, cell)
                            )
                        }
                    }
                }
            }

        }
    }
}
