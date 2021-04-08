package com.sumygg.anarts.arts.recursivetree

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import com.sumygg.anarts.arts.ArtsConfig
import com.sumygg.anarts.arts.BaseArts

/**
 * 绘制 RecursiveTree
 */
class RecursiveTree : BaseArts("Recursive Tree") {
    override fun onInit() {
    }

    override fun onUpdate(elapsedNanos: Long) {
    }

    override fun onDraw(config: ArtsConfig): DrawScope.() -> Unit {
        if (config !is RecursiveTreeConfig) {
            throw IllegalArgumentException("${this.name} must use ${RecursiveTreeConfig::class.simpleName}")
        }

        return {
            drawRect(Color.Black)

            // Start the tree from the bottom of the screen
            translate(this.size.width / 2, this.size.height) {
                // Draw a line 120 pixels
                drawLine(Color.White, start = Offset(0f, 0f), end = Offset(0f, -120f))
                // Move to the end of that line
                translate(0f, -120f) {
                    // Start the recursive branching!
                    branch(120f, config.degrees.value)
                }
            }
        }
    }

    private fun DrawScope.branch(h: Float, degree: Float) {
        // Each branch will be 2/3rds the size of the previous one
        val h = h * 0.66f
        // All recursive functions must have an exit condition!!!!
        // Here, ours is when the length of the branch is 2 pixels or less
        if (h > 2) {
            // Rotate by degree
            rotate(degree, Offset.Zero) {
                // Draw the branch
                drawLine(Color.White, start = Offset(0f, 0f), end = Offset(0f, -h))
                // Move to the end of the branch
                translate(0f, -h) {
                    // Ok, now call myself to draw two new branches!!
                    branch(h, degree)
                }
            }

            // Repeat the same thing, only branch off to the "left" this time!
            rotate(-degree, Offset.Zero) {
                drawLine(Color.White, start = Offset(0f, 0f), end = Offset(0f, -h))
                translate(0f, -h) {
                    branch(h, degree)
                }
            }
        }
    }
}
