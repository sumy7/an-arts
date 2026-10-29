package com.sumygg.anarts.ui

import com.sumygg.anarts.arts.Arts

/** Human-readable context for the existing artwork registry. */
data class ArtworkInfo(val title: String, val description: String, val category: String)

val Arts.info: ArtworkInfo
    get() = when (this) {
        Arts.BLACKHOLE -> ArtworkInfo("黑洞", "用噪声扰动层层环形曲线，形成流动的黑洞轮廓。", "曲线与噪声")
        Arts.BROWNIAN_MOTION -> ArtworkInfo("布朗运动", "连接随机游走的轨迹，观察不断延伸的运动路径。", "自然与模拟")
        Arts.CIRCLE_AND_DOT_GRID -> ArtworkInfo("圆与点阵", "小圆点沿圆环旋转，在规则网格中形成交错的节奏。", "几何与运动")
        Arts.CIRCLE_GRID_SCALE -> ArtworkInfo("呼吸圆阵", "圆形网格交替缩放，在实心与轮廓之间变化。", "几何与运动")
        Arts.CIRCLE_LOOP -> ArtworkInfo("圆环回路", "叠加位置和半径不断变化的圆环，绘出细密的曲线。", "曲线与噪声")
        Arts.CISTERCIAN_CLOCK -> ArtworkInfo("西多会时钟", "用西多会数字符号显示当前日期、时间和秒数。", "数字与符号")
        Arts.CISTERCIAN_NUMERALS -> ArtworkInfo("西多会数字", "将一个数字的千、百、十和个位组合成单个符号。", "数字与符号")
        Arts.DOTSWAVE -> ArtworkInfo("点的波浪", "随机散布的波形由细小圆点组成，噪声为它们增添纹理。", "曲线与噪声")
        Arts.HEARTS -> ArtworkInfo("心形涟漪", "层叠的彩色心形不断缩放，形成向外扩散的涟漪。", "几何与运动")
        Arts.IDENTICON -> ArtworkInfo("几何头像", "通过文本哈希生成颜色与对称图案，相同输入得到相同头像。", "数字与符号")
        Arts.MAZE -> ArtworkInfo("迷宫纹理", "在网格中随机连接两种对角线，生成迷宫般的纹理。", "几何与图案")
        Arts.POINT_RIBBON -> ArtworkInfo("点的缎带", "用三角函数安排点的位置和颜色，交织成细密的彩色缎带。", "曲线与噪声")
        Arts.RANDOM_CHORDS -> ArtworkInfo("随机和弦", "连接圆周上随机选取的两点，用弦线勾勒圆形纹理。", "几何与图案")
        Arts.RECURSIVE_TREE -> ArtworkInfo("递归树", "让树枝按相同规则不断分叉，观察角度如何改变树形。", "自然与模拟")
        Arts.ROTATING_SQUARES -> ArtworkInfo("旋转方块", "层叠的半透明方块以不同速度旋转，形成旋涡般的图案。", "几何与运动")
        Arts.SILKSKY -> ArtworkInfo("丝绸天空", "根据光源距离为圆点网格着色，形成柔和的天空色彩。", "几何与图案")
        Arts.SNOWFLAKES -> ArtworkInfo("飘雪", "大小不同的雪花沿着轻微摇摆的路径缓缓下落。", "自然与模拟")
        Arts.VOID_SETUP -> ArtworkInfo("太鼓回旋", "让太鼓角色随缩放与旋转进入画面，构成循环动画。", "几何与运动")
        Arts.YARN -> ArtworkInfo("线团", "通过噪声控制曲线上的点，叠加出蓬松而细密的线团。", "曲线与噪声")
    }

enum class ArtworkFilter { All, Animated, Static }

fun filterArtworks(query: String, filter: ArtworkFilter = ArtworkFilter.All): List<Arts> {
    val search = query.trim()
    return Arts.sortedValues.filter { artwork ->
        val matchesType = when (filter) {
            ArtworkFilter.All -> true
            ArtworkFilter.Animated -> artwork.animated
            ArtworkFilter.Static -> !artwork.animated
        }
        matchesType && (search.isEmpty() || with(artwork.info) {
            listOf(artwork.artsName, title, description, category).any { it.contains(search, ignoreCase = true) }
        })
    }
}
