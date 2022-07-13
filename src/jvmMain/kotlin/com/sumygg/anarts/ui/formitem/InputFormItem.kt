package com.sumygg.anarts.ui.formitem

/**
 * 此注解用于渲染一个输入框表单项
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY_GETTER)
annotation class InputFormItem(
    val title: String
)
