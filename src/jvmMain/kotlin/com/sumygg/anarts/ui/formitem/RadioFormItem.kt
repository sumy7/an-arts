package com.sumygg.anarts.ui.formitem

/**
 * 此注解用于渲染一个radio选择列表
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class RadioFormItem(
    val title: String,
    val values: Array<String>
)
