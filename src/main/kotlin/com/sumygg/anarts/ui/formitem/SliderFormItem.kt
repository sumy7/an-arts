package com.sumygg.anarts.ui.formitem

/**
 * 此注解用于渲染一个滑块表单项
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class SliderFormItem(
    val title: String,
    val min: Float,
    val max: Float,
    val steps: Int = 0,
    val fixToInt: Boolean = false,
)
