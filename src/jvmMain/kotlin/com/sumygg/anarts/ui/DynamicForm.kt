package com.sumygg.anarts.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.reflect.full.declaredMemberProperties

/**
 * 解析配置项中的注解，转换成表单项进行渲染。只解析MutableState类型上的表单项注解定义。
 *
 * @param formDefinition 表单项定义
 */
@Composable
fun DynamicForm(formDefinition: Any) {
    Column(modifier = Modifier.fillMaxWidth()) {
        formDefinition::class.declaredMemberProperties
            .map {
                val items = ArrayList<Triple<FormItemResolveType, Any, Any>>()
                val fieldValue = it.getter.call(formDefinition)
                it.getter.annotations.forEach { anno ->
                    items.add(
                        Triple(
                            FormItemResolveType.resolveType(anno, fieldValue!!),
                            anno,
                            fieldValue
                        )
                    )
                }
                items
            }
            .flatten()
            .filter { it.first != FormItemResolveType.UNRESOLVED }
            .apply {
                if (this.isEmpty()) {
                    Text("此Art无配置项")
                } else {
                    this.forEach {
                        ComposeFormItem(it.first, it.second, it.third)
                    }
                }
            }

    }

}
