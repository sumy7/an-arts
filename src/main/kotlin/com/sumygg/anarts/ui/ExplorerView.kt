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
        Box(modifier = Modifier.width(200.dp)) {
            Column {
                TopAppBar(
                    title = { Text(text = artDefine.value.name) },
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
                    onDraw = artDefine.value.draw(artConfig)
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


