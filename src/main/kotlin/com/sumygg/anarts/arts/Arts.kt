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
import kotlin.reflect.KClass

/**
 * arts声明，用于定义配对的arts绘制函数和表单配置项
 */
enum class Arts(
    val artsName: String,
    val animated: Boolean,
    val artsConfig: KClass<out ArtsConfig>,
    val arts: KClass<out BaseArts>
) {
    CIRCLE_LOOP("CircleLoop", false, CircleLoopConfig::class, CircleLoop::class),
    MAZE("Maze", false, MazeConfig::class, Maze::class),
    ROTATING_SQUARES("RotatingSquares", true, RotatingSquaresConfig::class, RotatingSquares::class),
    CIRCLE_AND_DOT_GRID("CircleAndDotGrid", true, CircleAndDotGridConfig::class, CircleAndDotGrid::class),
    CIRCLE_GRID_SCALE("CircleGridScale", true, CircleGridScaleConfig::class, CircleGridScale::class),
    YARN("Yarn", false, YarnConfig::class, Yarn::class),
    HEARTS("Hearts", true, HeartsConfig::class, Hearts::class),
    IDENTICON("Identicon", false, IdenticonConfig::class, Identicon::class),
    DOTSWAVE("DotsWave", false, DotsWaveConfig::class, DotsWave::class),
    CISTERCIAN_NUMERALS("CistercianNumerals", false, CistercianNumeralsConfig::class, CistercianNumerals::class),
    CISTERCIAN_CLOCK("CistercianClock", true, CistercianClockConfig::class, CistercianClock::class),
    VOID_SETUP("VoidSetup", true, VoidSetupConfig::class, VoidSetup::class),
    RECURSIVE_TREE("RecursiveTree", false, RecursiveTreeConfig::class, RecursiveTree::class),
//    CONTOUR_LINE("ContourLine", false, ContourLineConfig::class, ContourLine::class),
    SILKSKY("SilkSky", false, SilkSkyConfig::class, SilkSky::class),
    BLACKHOLE("BlackHole", false, BlackHoleConfig::class, BlackHole::class),
    SNOWFLAKES("SnowFlakes", true, SnowFlakesConfig::class, SnowFlakes::class),
    POINT_RIBBON("PointRibbon", false, PointRibbonConfig::class, PointRibbon::class),
    BROWNIAN_MOTION("BrownianMotion", true, BrownianMotionConfig::class, BrownianMotion::class),
    RANDOM_CHORDS("RandomChords", false, RandomChordsConfig::class, RandomChords::class),
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
