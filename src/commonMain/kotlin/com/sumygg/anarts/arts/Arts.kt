package com.sumygg.anarts.arts

import com.sumygg.anarts.arts.blackhole.BlackHole
import com.sumygg.anarts.arts.blackhole.BlackHoleConfig
import com.sumygg.anarts.arts.brownianmotion.BrownianMotion
import com.sumygg.anarts.arts.brownianmotion.BrownianMotionConfig
import com.sumygg.anarts.arts.circleanddotgrid.CircleAndDotGrid
import com.sumygg.anarts.arts.circleanddotgrid.CircleAndDotGridConfig
import com.sumygg.anarts.arts.circlegridscale.CircleGridScale
import com.sumygg.anarts.arts.circlegridscale.CircleGridScaleConfig
import com.sumygg.anarts.arts.circleloop.CircleLoop
import com.sumygg.anarts.arts.circleloop.CircleLoopConfig
import com.sumygg.anarts.arts.cistercianclock.CistercianClock
import com.sumygg.anarts.arts.cistercianclock.CistercianClockConfig
import com.sumygg.anarts.arts.cisterciannumerals.CistercianNumerals
import com.sumygg.anarts.arts.cisterciannumerals.CistercianNumeralsConfig
import com.sumygg.anarts.arts.contourline.ContourLine
import com.sumygg.anarts.arts.contourline.ContourLineConfig
import com.sumygg.anarts.arts.dotswave.DotsWave
import com.sumygg.anarts.arts.dotswave.DotsWaveConfig
import com.sumygg.anarts.arts.hearts.Hearts
import com.sumygg.anarts.arts.hearts.HeartsConfig
import com.sumygg.anarts.arts.identicon.Identicon
import com.sumygg.anarts.arts.identicon.IdenticonConfig
import com.sumygg.anarts.arts.maze.Maze
import com.sumygg.anarts.arts.maze.MazeConfig
import com.sumygg.anarts.arts.pointribbon.PointRibbon
import com.sumygg.anarts.arts.pointribbon.PointRibbonConfig
import com.sumygg.anarts.arts.randomchords.RandomChords
import com.sumygg.anarts.arts.randomchords.RandomChordsConfig
import com.sumygg.anarts.arts.recursivetree.RecursiveTree
import com.sumygg.anarts.arts.recursivetree.RecursiveTreeConfig
import com.sumygg.anarts.arts.rotatingsquares.RotatingSquares
import com.sumygg.anarts.arts.rotatingsquares.RotatingSquaresConfig
import com.sumygg.anarts.arts.silksky.SilkSky
import com.sumygg.anarts.arts.silksky.SilkSkyConfig
import com.sumygg.anarts.arts.snowflakes.SnowFlakes
import com.sumygg.anarts.arts.snowflakes.SnowFlakesConfig
import com.sumygg.anarts.arts.voidsetup.VoidSetup
import com.sumygg.anarts.arts.voidsetup.VoidSetupConfig
import com.sumygg.anarts.arts.yarn.Yarn
import com.sumygg.anarts.arts.yarn.YarnConfig

/**
 * arts声明，用于定义配对的arts绘制函数和表单配置项
 */
enum class Arts(
    val artsName: String,
    val animated: Boolean,
    val createConfig: () -> ArtsConfig,
    val createArts: () -> BaseArts
) {
    CIRCLE_LOOP("CircleLoop", false, ::CircleLoopConfig, ::CircleLoop),
    MAZE("Maze", false, ::MazeConfig, ::Maze),
    ROTATING_SQUARES("RotatingSquares", true, ::RotatingSquaresConfig, ::RotatingSquares),
    CIRCLE_AND_DOT_GRID("CircleAndDotGrid", true, ::CircleAndDotGridConfig, ::CircleAndDotGrid),
    CIRCLE_GRID_SCALE("CircleGridScale", true, ::CircleGridScaleConfig, ::CircleGridScale),
    YARN("Yarn", false, ::YarnConfig, ::Yarn),
    HEARTS("Hearts", true, ::HeartsConfig, ::Hearts),
    IDENTICON("Identicon", false, ::IdenticonConfig, ::Identicon),
    DOTSWAVE("DotsWave", false, ::DotsWaveConfig, ::DotsWave),
    CISTERCIAN_NUMERALS("CistercianNumerals", false, ::CistercianNumeralsConfig, ::CistercianNumerals),
    CISTERCIAN_CLOCK("CistercianClock", true, ::CistercianClockConfig, ::CistercianClock),
    VOID_SETUP("VoidSetup", true, ::VoidSetupConfig, ::VoidSetup),
    RECURSIVE_TREE("RecursiveTree", false, ::RecursiveTreeConfig, ::RecursiveTree),
//    CONTOUR_LINE("ContourLine", false, ::ContourLineConfig, ::ContourLine),
    SILKSKY("SilkSky", false, ::SilkSkyConfig, ::SilkSky),
    BLACKHOLE("BlackHole", false, ::BlackHoleConfig, ::BlackHole),
    SNOWFLAKES("SnowFlakes", true, ::SnowFlakesConfig, ::SnowFlakes),
    POINT_RIBBON("PointRibbon", false, ::PointRibbonConfig, ::PointRibbon),
    BROWNIAN_MOTION("BrownianMotion", true, ::BrownianMotionConfig, ::BrownianMotion),
    RANDOM_CHORDS("RandomChords", false, ::RandomChordsConfig, ::RandomChords),
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
