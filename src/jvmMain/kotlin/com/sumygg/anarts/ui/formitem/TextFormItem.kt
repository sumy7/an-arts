package com.sumygg.anarts.ui.formitem

/**
 * 此注解用于渲染一个文本表单项
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class TextFormItem(
    val title: String
)
