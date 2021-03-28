package com.sumygg.anarts.ui

import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.sumygg.anarts.ui.formitem.InputFormItem
import com.sumygg.anarts.ui.formitem.SliderFormItem
import com.sumygg.anarts.ui.formitem.TextFormItem

/**
 * 判断是否formItemConfig是不是一个能够解析的类型
 *
 * @param formItemConfig 期待的表单注解类型
 * @return 是否能够解析
 */
fun canResolvedFormItem(formItemConfig: Any): Boolean {
    return when (formItemConfig) {
        is InputFormItem -> {
            true
        }
        is TextFormItem -> {
            true
        }
        is SliderFormItem -> {
            true
        }
        else -> {
            false
        }
    }
}

/**
 * 根据表单类型和数据，渲染表单项
 *
 * @param formItemConfig 表单项配置
 * @param state 表单项数据
 */
@Composable
fun <T> ComposeFormItem(formItemConfig: Any, state: MutableState<T>) {
    when (formItemConfig) {
        is InputFormItem -> {
            getInputFormItem(formItemConfig, state)
        }
        is TextFormItem -> {
            getTextFormItem(formItemConfig, state)
        }
        is SliderFormItem -> {
            getSliderFormItem(formItemConfig, state)
        }
        else -> {
            throw IllegalArgumentException("Unknown FromItem config type ${formItemConfig::class}")
        }
    }
}

/**
 * 渲染文本类型的表单项，文本类型的表单项是由一个禁用的文本框组成
 *
 * @param formItem 文本类型表单项
 * @param state 文本数据
 */
@Composable
fun getTextFormItem(formItem: TextFormItem, state: MutableState<*>) {
    if (state.value !is String) {
        throw IllegalArgumentException("FormItem type=TEXT must provide [String] type state value")
    }
    Text(text = formItem.title)

    val textState = state as MutableState<String>
    TextField(value = textState.value, enabled = false, onValueChange = {})
}

/**
 * 渲染一个输入框表单项，用于输入文本信息
 *
 * @param formItem 输入框类型表单项
 * @param state 输入框数据
 */
@Composable
fun getInputFormItem(formItem: InputFormItem, state: MutableState<*>) {
    if (state.value !is String) {
        throw IllegalArgumentException("FormItem type=INPUT must provide [String] type state value")
    }
    Text(text = formItem.title)

    val inputState = state as MutableState<String>
    TextField(value = inputState.value, onValueChange = {
        inputState.value = it
    })
}

/**
 * 渲染一个滑块表单项，用于输入带有上下界的数值
 *
 * @param formItem 滑块类型表单项
 * @param state 滑块数据
 */
@Composable
fun getSliderFormItem(formItem: SliderFormItem, state: MutableState<*>) {
    if (state.value !is Float) {
        throw IllegalArgumentException("FormItem type=Slide must provide [Float] type state value")
    }
    Text(text = "${formItem.title} = ${state.value}")

    val sliderState = state as MutableState<Float>
    Slider(value = sliderState.value, onValueChange = {
        sliderState.value = it
    }, valueRange = formItem.min..formItem.max)
}
