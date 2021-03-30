package com.sumygg.anarts.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sumygg.anarts.arts.Arts
import kotlin.reflect.full.createInstance

/**
 * 渲染一个左右分栏的界面，左边展示配置信息，右边展示输出结果
 */
@Composable
fun ExplorerView() {
    val artDefine: MutableState<Arts> = remember { mutableStateOf(Arts.CIRCLE_LOOP) }
    val artConfig = artDefine.value.artsConfig.createInstance()
    val arts = artDefine.value.arts.createInstance()
    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.width(300.dp)) {
            Column {
                TopAppBar(
                    title = { Text(text = artDefine.value.artsName) },
                    elevation = 8.dp,
                    navigationIcon = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Default.List, contentDescription = "ArtList")
                        }
                    }
                )
                ExplorerArtConfigView(artConfig)
            }
        }
        Box {
            Column {
                TopAppBar(
                    title = { Text("") },
                    elevation = 8.dp,
                    navigationIcon = {
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
//                        }
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Default.AccountCircle, contentDescription = "Stop")
//                        }
                    },
                    actions = {
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Save")
//                        }
                    }
                )
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = arts.onDraw(artConfig)
                )
            }
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
    Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
        DynamicForm(artConfig)
    }
}


