package com.sumygg.anarts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.jvmErasure

/**
 * 解析配置项中的注解，转换成表单项进行渲染。只解析MutableState类型上的表单项注解定义。
 *
 * @param formDefinition 表单项定义
 */
@Composable
fun DynamicForm(formDefinition: Any) {
    Column(modifier = Modifier.fillMaxWidth()) {
        formDefinition::class.declaredMemberProperties
            .filter {
                it.returnType.jvmErasure == MutableState::class
            }
            .map {
                val items = ArrayList<Pair<Any, MutableState<*>>>()
                val fieldValue = it.getter.call(formDefinition) as MutableState<*>
                it.getter.annotations.forEach { anno ->
                    if (canResolvedFormItem(anno)) {
                        items.add(anno to fieldValue)
                    }
                }
                items
            }
            .flatten()
            .forEach {
                ComposeFormItem(it.first, it.second)
            }
    }

}
