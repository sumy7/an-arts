package com.sumygg.anarts.ui

import com.sumygg.anarts.arts.Arts
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ArtworkInfoTest {
    @Test
    fun everyArtworkHasUsefulDisplayMetadata() {
        for (artwork in Arts.entries) {
            assertTrue(artwork.info.title.isNotBlank(), artwork.artsName)
            assertTrue(artwork.info.description.isNotBlank(), artwork.artsName)
            assertTrue(artwork.info.category.isNotBlank(), artwork.artsName)
        }
    }

    @Test
    fun emptyQueryKeepsTheFullAlphabeticalLibrary() {
        assertEquals(Arts.sortedValues, filterArtworks(""))
        assertEquals(Arts.sortedValues, filterArtworks("   "))
    }

    @Test
    fun queryMatchesEnglishChineseAndDescription() {
        assertEquals(listOf(Arts.BLACKHOLE), filterArtworks("  bLaCkHoLe  "))
        assertEquals(listOf(Arts.BLACKHOLE), filterArtworks("黑洞"))
        assertEquals(listOf(Arts.IDENTICON), filterArtworks("哈希"))
        assertTrue(filterArtworks("自然").isNotEmpty())
    }

    @Test
    fun typeFiltersCombineWithSearch() {
        val animated = filterArtworks("", ArtworkFilter.Animated)
        val static = filterArtworks("", ArtworkFilter.Static)
        assertTrue(animated.isNotEmpty())
        assertTrue(animated.all { it.animated })
        assertTrue(static.all { !it.animated })
        assertEquals(Arts.entries.size, animated.size + static.size)
        assertEquals(listOf(Arts.CIRCLE_AND_DOT_GRID, Arts.CIRCLE_GRID_SCALE), filterArtworks("circle", ArtworkFilter.Animated))
        assertEquals(listOf(Arts.CIRCLE_LOOP), filterArtworks("circle", ArtworkFilter.Static))
        assertTrue(filterArtworks("黑洞", ArtworkFilter.Animated).isEmpty())
    }

    @Test
    fun noMatchReturnsAnEmptyList() {
        assertTrue(filterArtworks("no-such-artwork").isEmpty())
    }
}
