package com.sumygg.anarts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.ui.formitem.*
import com.sumygg.anarts.utils.reverse
import com.sumygg.anarts.utils.toAndroidxColor
import com.sumygg.anarts.utils.toHexString
import com.sumygg.anarts.utils.toSwingColor
import javax.swing.JColorChooser
import kotlin.reflect.KClass

/**
 * 表单类型和响应值对照表
 */
enum class FormItemResolveType(
    val itemConfigType: KClass<*>,
    val mutableType: KClass<*>
) {
    INPUT(InputFormItem::class, MutableState::class),
    TEXT(TextFormItem::class, MutableState::class),
    SLIDER(SliderFormItem::class, MutableState::class),
    COLOR_CHOOSE(ColorChooseFormItem::class, MutableState::class),
    RADIO(RadioFormItem::class, MutableState::class),
    COLOR_LIST(ColorListFormItem::class, MutableList::class),
    UNRESOLVED(Any::class, Any::class),
    ;

    companion object {
        /**
         * 判断是否formItemConfig是不是一个能够解析的类型
         *
         * @param formItemConfig 期待的表单注解类型
         * @param state 期待解析的响应值
         * @return 能够解析的类型
         */
        fun resolveType(formItemConfig: Any, state: Any): FormItemResolveType {
            for (item in values()) {
                if (item.itemConfigType.isInstance(formItemConfig) &&
                    item.mutableType.isInstance(state)
                ) {
                    return item
                }
            }
            return UNRESOLVED
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
fun ComposeFormItem(resolveType: FormItemResolveType, formItemConfig: Any, state: Any) {
    when (resolveType) {
        FormItemResolveType.INPUT -> {
            getInputFormItem(formItemConfig as InputFormItem, state as MutableState<*>)
        }
        FormItemResolveType.TEXT -> {
            getTextFormItem(formItemConfig as TextFormItem, state as MutableState<*>)
        }
        FormItemResolveType.SLIDER -> {
            getSliderFormItem(formItemConfig as SliderFormItem, state as MutableState<*>)
        }
        FormItemResolveType.COLOR_CHOOSE -> {
            getColorChooseFormItem(formItemConfig as ColorChooseFormItem, state as MutableState<*>)
        }
        FormItemResolveType.RADIO -> {
            getRadioFormItem(formItemConfig as RadioFormItem, state as MutableState<*>)
        }
        FormItemResolveType.COLOR_LIST -> {
            getColorListFormItem(formItemConfig as ColorListFormItem, state as MutableList<*>)
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
    if (state.value !is Number) {
        throw IllegalArgumentException("FormItem type=Slide must provide [Number] type state value")
    }
    Text(text = "${formItem.title} = ${state.value}")

    val sliderState = state as MutableState<Number>
    Slider(
        value = sliderState.value.toFloat(),
        onValueChange = {
            if (formItem.fixToInt) {
                sliderState.value = it.toInt()
            } else {
                sliderState.value = it
            }
        },
        valueRange = formItem.min..formItem.max,
        steps = formItem.steps,
    )
}

/**
 * 渲染一个颜色选择表单项，用于选择单个颜色
 *
 * @param formItem 颜色选择表单项定义
 * @param state 颜色数据
 */
@Composable
fun getColorChooseFormItem(formItem: ColorChooseFormItem, state: MutableState<*>) {
    if (state.value !is Color) {
        throw IllegalArgumentException("FormItem type=ColorChoose must provide [Color] type state value")
    }

    val colorChooseState = state as MutableState<Color>

    Text(text = formItem.title)

    Button(
        onClick = {
            val color = JColorChooser.showDialog(null, "请选择颜色", colorChooseState.value.toSwingColor())
            if (color != null) {
                colorChooseState.value = color.toAndroidxColor()
            }
        },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = colorChooseState.value,
            contentColor = colorChooseState.value.reverse()
        )
    ) {
        Text(text = colorChooseState.value.toHexString())
    }
}

/**
 * 渲染一个单项选择列表
 */
@Composable
fun getRadioFormItem(formItem: RadioFormItem, state: MutableState<*>) {
    if (state.value !is String) {
        throw IllegalArgumentException("FormItem type=Radio must provide [String] type state value")
    }

    Text(text = formItem.title)

    val radioChooseState = state as MutableState<String>

    for (item in formItem.values) {
        Row {
            RadioButton(selected = radioChooseState.value == item,
                onClick = { radioChooseState.value = item })
            Text(text = item)
        }
    }
}

/**
 * 渲染一个颜色选择列表
 */
@Composable
fun getColorListFormItem(formItem: ColorListFormItem, state: MutableList<*>) {

    val colorListState = state as MutableList<Color>

    Row {
        Text(text = formItem.title)
        IconButton(onClick = {
            colorListState.add(Color.Black)
        }) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
    }

    Column {
        colorListState.forEachIndexed { i, _ ->
            Row {
                Button(
                    onClick = {
                        val color = JColorChooser.showDialog(null, "请选择颜色", colorListState[i].toSwingColor())
                        if (color != null) {
                            colorListState[i] = color.toAndroidxColor()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorListState[i],
                        contentColor = colorListState[i].reverse()
                    )
                ) {
                    Text(text = colorListState[i].toHexString())
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (formItem.canEmpty || (!formItem.canEmpty && state.size > 1)) {
                    IconButton(onClick = {
                        colorListState.removeAt(i)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null
                        )
                    }
                }
            }
            Divider()
        }
    }
}
