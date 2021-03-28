package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumygg.anarts.arts.ALL_ARTS
import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.arts.ArtsConfig
import kotlin.reflect.full.createInstance

/**
 * 渲染一个左右分栏的界面，左边展示配置信息，右边展示输出结果
 */
@Composable
fun ExplorerView() {
    val artDefine: MutableState<Arts> = remember { mutableStateOf(ALL_ARTS[1]) }
    val artConfig = artDefine.value.config.createInstance() as ArtsConfig
    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.width(200.dp), contentAlignment = Alignment.Center) {
            Row(modifier = Modifier.fillMaxWidth().background(color = Color.Magenta)) {
                Text(
                    artDefine.value.name,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .padding(vertical = 12.dp, horizontal = 4.dp)

                )
            }
            ExplorerArtConfigView(artConfig)
        }
        Box(modifier = Modifier.padding(10.dp)) {
            Canvas(
                modifier = Modifier.fillMaxSize(),
                onDraw = artDefine.value.draw(artConfig)
            )
        }
    }
}

/**
 * 渲染配置信息
 *
 * @param artConfig 配置信息表单项数据
 */
@Composable
fun ExplorerArtConfigView(artConfig: Any) {
    DynamicForm(artConfig)
}


