package com.sumygg.anarts.utils

import kotlin.random.Random

/**
 * 在整数范围内生成一个随机数
 */
fun ClosedRange<Int>.random() = Random.Default.nextInt(endInclusive - start) + start

/**
 * 在长整型范围内生成一个随机数
 */
fun ClosedRange<Long>.random() = Random.Default.nextLong(endInclusive - start) + start

/**
 * 在浮点数范围内生成一个随机数
 */
fun ClosedRange<Float>.random() = Random.Default.nextFloat() * (endInclusive - start) + start

/**
 * 在双精度浮点数范围内生成一个随机数
 */
fun ClosedRange<Double>.random() = Random.Default.nextDouble(endInclusive - start) + start
