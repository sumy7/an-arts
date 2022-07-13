package com.sumygg.anarts.utils

import java.util.concurrent.ThreadLocalRandom

/**
 * 在整数范围内生成一个随机数
 */
fun ClosedRange<Int>.random() = ThreadLocalRandom.current().nextInt(endInclusive - start) + start

/**
 * 在长整型范围内生成一个随机数
 */
fun ClosedRange<Long>.random() = ThreadLocalRandom.current().nextLong(endInclusive - start) + start

/**
 * 在浮点数范围内生成一个随机数
 */
fun ClosedRange<Float>.random() = ThreadLocalRandom.current().nextFloat() * (endInclusive - start) + start

/**
 * 在双精度浮点数范围内生成一个随机数
 */
fun ClosedRange<Double>.random() = ThreadLocalRandom.current().nextDouble(endInclusive - start) + start
