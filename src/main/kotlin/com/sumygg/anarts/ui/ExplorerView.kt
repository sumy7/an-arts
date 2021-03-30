package com.sumygg.anarts.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.utils.withoutWidthConstraints
import kotlin.reflect.full.createInstance

/**
 * 渲染一个左右分栏的界面，左边展示配置信息，右边展示输出结果
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExplorerView() {
    val artsState: MutableState<Arts> = remember { mutableStateOf(Arts.sortedValues.first()) }
    val artConfig = artsState.value.artsConfig.createInstance()
    val arts = artsState.value.arts.createInstance()

    val isArtsListOpenState: MutableState<Boolean> = remember { mutableStateOf(false) }

    Row(Modifier.fillMaxSize()) {
        Box(modifier = Modifier.width(300.dp)) {
            Column {
                TopAppBar(
                    title = { Text(text = artsState.value.artsName) },
                    elevation = 8.dp,
                    navigationIcon = {
                        IconButton(onClick = {
                            isArtsListOpenState.value = !isArtsListOpenState.value
                        }) {
                            Icon(Icons.Default.List, contentDescription = "ArtList")
                        }
                    }
                )
                if (isArtsListOpenState.value) {
                    ArtsListView(artsState, isArtsListOpenState)
                } else {
                    ArtsConfigView(artConfig)
                }
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
private fun ArtsConfigView(artConfig: Any) {
    Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
        DynamicForm(artConfig)
    }
}

/**
 * 渲染arts列表
 */
@ExperimentalFoundationApi
@Composable
private fun ArtsListView(
    artsState: MutableState<Arts>,
    isArtsListOpenState: MutableState<Boolean>
) {
    Box {
        with(LocalDensity.current) {
            val scrollState = rememberLazyListState()

            val fontSize = 14.sp
            val lineHeight = fontSize.toDp() * 1.5f

            val sortedItems = Arts.sortedValues
            LazyColumn(
                modifier = Modifier.fillMaxSize().withoutWidthConstraints(),
                state = scrollState
            ) {
                items(sortedItems) {
                    ArtsListItemViewImpl(it, artsState, fontSize, lineHeight) {
                        isArtsListOpenState.value = false
                    }
                }
            }

            VerticalScrollbar(
                rememberScrollbarAdapter(scrollState, sortedItems.size, lineHeight),
                Modifier.align(Alignment.CenterEnd),
            )
        }
    }

}

/**
 * 渲染arts列表项
 */
@Composable
private fun ArtsListItemViewImpl(
    arts: Arts,
    artsState: MutableState<Arts>,
    fontSize: TextUnit,
    height: Dp,
    artListClickCallback: () -> Unit
) {
    val isCurrent = artsState.value == arts

    Row(
        modifier = Modifier
            .wrapContentHeight()
            .clickable {
                artsState.value = arts
                artListClickCallback()
            }
            .height(height)
            .padding(start = 16.dp)
    ) {
        var inFocus by remember { mutableStateOf(false) }
        val textColor = LocalContentColor.current.let {
            when {
                isCurrent -> it
                inFocus -> it.copy(alpha = 0.6f)
                else -> it.copy(alpha = 0.4f)
            }
        }

        Text(
            text = arts.artsName,
            color = textColor,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .clipToBounds()
                .pointerMoveFilter(
                    onEnter = {
                        inFocus = true
                        true
                    },
                    onExit = {
                        inFocus = false
                        true
                    }
                ),
            softWrap = true,
            fontSize = fontSize,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}

