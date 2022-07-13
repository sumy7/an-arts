package com.sumygg.anarts.ui.formitem

/**
 * 此注解用于渲染一个颜色选择表单项
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class ColorChooseFormItem(
    val title: String
)
