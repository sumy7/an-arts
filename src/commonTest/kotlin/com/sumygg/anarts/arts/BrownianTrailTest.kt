package com.sumygg.anarts.arts

import androidx.compose.ui.geometry.Offset
import com.sumygg.anarts.arts.brownianmotion.BrownianTrail
import kotlin.test.Test
import kotlin.test.assertEquals

class BrownianTrailTest {
    @Test
    fun keepsTheSameAgeOrderAsShiftingHistoryAcrossManyWraps() {
        for (capacity in listOf(1, 2, 17, 2000)) {
            val trail = BrownianTrail(capacity)
            val shifted = MutableList(capacity) { Offset(300f, 300f) }
            repeat(capacity * 3 + 7) { step ->
                val point = Offset(step * .5f, -step * 2f)
                shifted.removeAt(0)
                shifted.add(point)
                trail.append(point.x, point.y)
                assertEquals(shifted.first(), trail.pointAt(0))
                assertEquals(shifted.last(), trail.pointAt(capacity - 1))
                if (capacity <= 17 || step % (capacity / 4) == 0 || step == capacity * 3 + 6) {
                    for (index in 0 until capacity) assertEquals(shifted[index], trail.pointAt(index), "capacity=$capacity step=$step index=$index")
                }
            }
        }
    }

    @Test
    fun resetRestoresTheInitialStationaryHistoryAfterWrapping() {
        val trail = BrownianTrail(3)
        repeat(10) { trail.append(it.toFloat(), -it.toFloat()) }
        trail.reset()
        repeat(3) { assertEquals(Offset(300f, 300f), trail.pointAt(it)) }
        trail.append(310f, 290f)
        assertEquals(Offset(300f, 300f), trail.pointAt(0))
        assertEquals(Offset(310f, 290f), trail.pointAt(2))
    }
}
