package com.sumygg.anarts.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.zIndex
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.sumygg.anarts.arts.Arts
import com.sumygg.anarts.viewmodel.ArtsModel

@Composable
fun ExplorerView(darkTheme: Boolean, onToggleTheme: () -> Unit) {
    val model = remember { ArtsModel(Arts.sortedValues.first()) }
    val workspace = remember { WorkspaceState() }
    val colorEditor = LocalColorEditor.current
    val searchFocus = remember { FocusRequester() }
    val canvasFocus = remember { FocusRequester() }
    val panelFocus = remember { FocusRequester() }
    var query by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(ArtworkFilter.All) }
    var searchRequest by remember { mutableIntStateOf(0) }
    var squareCanvas by remember { mutableStateOf(true) }
    var canvasSize by remember { mutableStateOf(IntSize.Zero) }
    val palette = LocalWorkspacePalette.current
    val artworks = remember(query, filter) { filterArtworks(query, filter) }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val layout = workspaceLayout(maxWidth.value)
        val sheetWidth = minOf(320.dp, maxWidth - 32.dp)
        LaunchedEffect(layout) { workspace.onLayoutChanged(layout) }
        LaunchedEffect(workspace.overlay) {
            if (workspace.overlay != null) {
                withFrameNanos { }
                if (workspace.overlay == WorkspacePanel.Library) searchFocus.requestFocus() else panelFocus.requestFocus()
            } else {
                withFrameNanos { }
                canvasFocus.requestFocus()
            }
        }
        LaunchedEffect(searchRequest) {
            if (searchRequest > 0) {
                withFrameNanos { }
                searchFocus.requestFocus()
            }
        }
        val openSearch = {
            workspace.openSearch(layout)
            searchRequest++
        }
        val dismissPanel: () -> Unit = {
            workspace.dismissOverlay()
            canvasFocus.requestFocus()
        }
        val selectArtwork: (Arts) -> Unit = { art ->
            if (model.artsType != art) model.switchTo(art)
            workspace.dismissOverlay()
            canvasFocus.requestFocus()
        }
        val library: @Composable () -> Unit = {
            ArtworkLibrary(
                model.artsType, artworks, query, filter, searchFocus,
                onQueryChange = { query = it }, onFilterChange = { filter = it },
                onSelect = selectArtwork,
                onClose = { workspace.toggleLibrary(layout) },
            )
        }
        val inspector: @Composable () -> Unit = {
            ArtworkInspector(model, onClose = {
                if (layout == WorkspaceLayout.Wide) workspace.toggleInspector(layout) else dismissPanel()
            })
        }

        val handleShortcut: (KeyEvent) -> Boolean = { event ->
            when {
                event.type != KeyEventType.KeyDown -> false
                event.key == Key.Escape && workspace.overlay != null -> { dismissPanel(); true }
                (event.isCtrlPressed || event.isMetaPressed) && !event.isShiftPressed && !event.isAltPressed -> when (event.key) {
                    Key.K -> { openSearch(); true }
                    Key.B -> { workspace.toggleLibrary(layout); true }
                    Key.Comma -> { workspace.toggleInspector(layout); true }
                    else -> false
                }
                else -> false
            }
        }
        Row(Modifier.fillMaxSize().onPreviewKeyEvent(handleShortcut)
            .then(if (workspace.overlay != null) Modifier.clearAndSetSemantics { } else Modifier)) {
            if (layout != WorkspaceLayout.Compact) {
                NavigationRibbon(
                    libraryActive = workspace.libraryShown(layout),
                    inspectorActive = workspace.inspectorShown(layout) || workspace.overlay == WorkspacePanel.Inspector,
                    darkTheme = darkTheme,
                    onLibrary = { workspace.toggleLibrary(layout) },
                    onInspector = { workspace.toggleInspector(layout) },
                    onToggleTheme = onToggleTheme,
                )
            }
            Column(Modifier.weight(1f).fillMaxHeight()) {
                WorkspaceHeader(
                    compact = layout == WorkspaceLayout.Compact,
                    category = model.artsType.info.category,
                    darkTheme = darkTheme,
                    focusMode = workspace.focusMode,
                    onLibrary = { workspace.toggleLibrary(layout) },
                    onInspector = { workspace.toggleInspector(layout) },
                    onToggleTheme = onToggleTheme,
                    onFocus = { workspace.toggleFocusMode() },
                )
                Row(Modifier.weight(1f).fillMaxWidth()) {
                    if (workspace.libraryShown(layout)) {
                        Box(Modifier.width(232.dp).fillMaxHeight()) { library() }
                        PanelDivider()
                    }
                    Column(Modifier.weight(1f).fillMaxHeight()) {
                        CanvasToolbar(model)
                        CanvasStage(
                            model, squareCanvas, onSquareChange = { squareCanvas = it },
                            canvasFocus = canvasFocus,
                            onCanvasSize = { canvasSize = it },
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                        )
                    }
                    if (workspace.inspectorShown(layout)) {
                        PanelDivider()
                        Box(Modifier.width(280.dp).fillMaxHeight()) { inspector() }
                    }
                }
                WorkspaceStatus(model, canvasSize, layout)
            }
        }
        workspace.overlay?.let { panel ->
            Box(Modifier.fillMaxSize().clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null,
                onClick = dismissPanel,
            ).background(Color.Black.copy(alpha = 0.48f)).semantics { contentDescription = "关闭侧栏" })
            Surface(
                modifier = Modifier.align(if (panel == WorkspacePanel.Library) Alignment.CenterStart else Alignment.CenterEnd)
                    .width(sheetWidth).fillMaxHeight()
                    .onPreviewKeyEvent(handleShortcut)
                    .focusRequester(panelFocus)
                    .focusProperties { onExit = { if (workspace.overlay != null && colorEditor.request == null) cancelFocusChange() } }
                    .focusGroup()
                    .semantics { paneTitle = if (panel == WorkspacePanel.Library) "作品库" else "参数" },
                shape = RoundedCornerShape(0.dp),
                color = MaterialTheme.colors.surface,
                border = BorderStroke(1.dp, palette.border),
                elevation = 8.dp,
            ) {
                if (panel == WorkspacePanel.Library) library() else inspector()
            }
        }
    }
}

@Composable
private fun NavigationRibbon(
    libraryActive: Boolean, inspectorActive: Boolean, darkTheme: Boolean,
    onLibrary: () -> Unit, onInspector: () -> Unit, onToggleTheme: () -> Unit,
) {
    val palette = LocalWorkspacePalette.current
    Column(
        Modifier.width(48.dp).fillMaxHeight().zIndex(2f).background(palette.rail).padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(Modifier.height(40.dp), contentAlignment = Alignment.Center) {
            Icon(ArtIcons.Artwork, null, Modifier.size(27.dp), tint = MaterialTheme.colors.primary)
        }
        Spacer(Modifier.height(16.dp))
        WorkspaceIconButton(ArtIcons.List, "切换作品库（Ctrl / ⌘ B）", onLibrary, libraryActive)
        Spacer(Modifier.height(4.dp))
        WorkspaceIconButton(ArtIcons.Inspector, "切换参数面板（Ctrl / ⌘ ,）", onInspector, inspectorActive)
        Spacer(Modifier.weight(1f))
        WorkspaceIconButton(ArtIcons.Appearance, if (darkTheme) "切换浅色外观" else "切换深色外观", onToggleTheme)
    }
    PanelDivider()
}

@Composable
private fun WorkspaceHeader(
    compact: Boolean, category: String, darkTheme: Boolean, focusMode: Boolean,
    onLibrary: () -> Unit, onInspector: () -> Unit, onToggleTheme: () -> Unit, onFocus: () -> Unit,
) {
    val palette = LocalWorkspacePalette.current
    Row(
        Modifier.fillMaxWidth().height(56.dp).zIndex(2f).background(MaterialTheme.colors.surface).padding(horizontal = if (compact) 4.dp else 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (compact) WorkspaceIconButton(ArtIcons.List, "打开作品库", onLibrary)
        Text("an·arts", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        if (!compact) {
            Text("/", Modifier.padding(horizontal = 16.dp), color = palette.muted)
            Text(category, color = palette.muted, style = MaterialTheme.typography.caption)
        }
        Spacer(Modifier.weight(1f))
        if (compact) {
            WorkspaceIconButton(ArtIcons.Appearance, if (darkTheme) "切换浅色外观" else "切换深色外观", onToggleTheme)
            WorkspaceIconButton(ArtIcons.Inspector, "打开参数面板", onInspector)
        }
        WorkspaceIconButton(ArtIcons.Focus, if (focusMode) "退出专注模式" else "专注模式", onFocus, focusMode)
    }
    Divider(color = palette.border)
}

@Composable
private fun ArtworkLibrary(
    selected: Arts, artworks: List<Arts>, query: String, filter: ArtworkFilter,
    searchFocus: FocusRequester, onQueryChange: (String) -> Unit,
    onFilterChange: (ArtworkFilter) -> Unit, onSelect: (Arts) -> Unit, onClose: () -> Unit,
) {
    val palette = LocalWorkspacePalette.current
    Column(Modifier.fillMaxSize().background(palette.sidebar)) {
        Row(Modifier.fillMaxWidth().height(56.dp).zIndex(2f).padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("作品库", style = MaterialTheme.typography.subtitle2)
            Text("${Arts.entries.size}", Modifier.padding(start = 8.dp)
                .background(palette.selection, RoundedCornerShape(5.dp)).padding(horizontal = 6.dp, vertical = 1.dp),
                color = palette.muted, style = MaterialTheme.typography.caption)
            Spacer(Modifier.weight(1f))
            WorkspaceIconButton(ArtIcons.Close, "关闭作品库", onClose)
        }
        ShadcnInput(
            value = query, onValueChange = onQueryChange,
            label = "搜索作品", placeholder = "搜索作品…",
            modifier = Modifier.padding(horizontal = 12.dp).fillMaxWidth().focusRequester(searchFocus),
            leading = { Icon(ArtIcons.Search, null, Modifier.size(16.dp), tint = palette.muted) },
            trailing = {
                if (query.isNotEmpty()) {
                    ShadcnIconButton(ArtIcons.Close, "清除搜索", { onQueryChange("") }, modifier = Modifier.size(28.dp))
                }
            },
        )
        val filterFocus = remember { ArtworkFilter.entries.map { FocusRequester() } }
        var filterFocusRequest by remember { mutableStateOf<ArtworkFilter?>(null) }
        LaunchedEffect(filter, filterFocusRequest) {
            if (filterFocusRequest == filter) {
                withFrameNanos { }
                filterFocus[filter.ordinal].requestFocus()
                filterFocusRequest = null
            }
        }
        Row(Modifier.fillMaxWidth().padding(12.dp)
            .background(palette.selection, RoundedCornerShape(8.dp)).padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            ArtworkFilter.entries.forEachIndexed { index, item ->
                val label = when (item) { ArtworkFilter.All -> "全部"; ArtworkFilter.Animated -> "动态"; ArtworkFilter.Static -> "静态" }
                val interactions = remember { MutableInteractionSource() }
                val hovered by interactions.collectIsHoveredAsState()
                val focused by interactions.collectIsFocusedAsState()
                Box(
                    Modifier.weight(1f).height(30.dp).focusRequester(filterFocus[index])
                        .focusProperties { canFocus = item == filter }
                        .onPreviewKeyEvent { event ->
                            val next = if (event.type == KeyEventType.KeyDown &&
                                !event.isCtrlPressed && !event.isMetaPressed && !event.isAltPressed && !event.isShiftPressed) when (event.key) {
                                Key.DirectionRight -> (index + 1) % ArtworkFilter.entries.size
                                Key.DirectionLeft -> (index + ArtworkFilter.entries.size - 1) % ArtworkFilter.entries.size
                                Key.MoveHome -> 0
                                Key.MoveEnd -> ArtworkFilter.entries.lastIndex
                                else -> null
                            } else null
                            if (next != null) {
                                val nextFilter = ArtworkFilter.entries[next]
                                onFilterChange(nextFilter)
                                filterFocusRequest = nextFilter
                                true
                            } else false
                        }
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (item == filter) MaterialTheme.colors.surface else if (hovered) palette.raised else Color.Transparent)
                        .then(if (focused) Modifier.border(2.dp, palette.ring, RoundedCornerShape(5.dp)) else Modifier)
                        .hoverable(interactions)
                        .selectable(item == filter, interactionSource = interactions, indication = null,
                            role = Role.Tab, onClick = { onFilterChange(item) }),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(label, color = if (item == filter) MaterialTheme.colors.onSurface else palette.muted,
                        fontWeight = if (item == filter) FontWeight.Medium else FontWeight.Normal,
                        style = MaterialTheme.typography.caption)
                }
            }
        }
        if (artworks.isEmpty()) {
            Column(Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(ArtIcons.Search, null, Modifier.padding(vertical = 16.dp).size(28.dp), tint = palette.muted)
                Text("未找到作品", style = MaterialTheme.typography.subtitle2)
                Text("试试其他关键词或筛选条件", color = palette.muted, style = MaterialTheme.typography.caption)
                ShadcnButton(onClick = { onQueryChange(""); onFilterChange(ArtworkFilter.All) },
                    modifier = Modifier.padding(top = 12.dp), variant = ShadcnButtonVariant.Outline) { Text("清除筛选") }
            }
        } else {
            val scrollState = rememberLazyListState()
            Box(Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn(Modifier.fillMaxSize().padding(horizontal = 8.dp), state = scrollState, verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    items(artworks, key = { it.name }) { art ->
                        val active = art == selected
                        val interactions = remember { MutableInteractionSource() }
                        val hovered by interactions.collectIsHoveredAsState()
                        val focused by interactions.collectIsFocusedAsState()
                        Row(
                            Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(6.dp))
                                .background(if (active || hovered) palette.selection else Color.Transparent)
                                .then(if (focused) Modifier.border(2.dp, palette.ring, RoundedCornerShape(6.dp)) else Modifier)
                                .hoverable(interactions)
                                .selectable(active, interactionSource = interactions, indication = null,
                                    role = Role.Button, onClick = { onSelect(art) })
                                .semantics { contentDescription = "${art.info.title}，${art.artsName}，${if (art.animated) "动画作品" else "静态作品"}" }
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(ArtIcons.Artwork, null, Modifier.size(18.dp), tint = if (active) MaterialTheme.colors.onSurface else palette.muted)
                            Spacer(Modifier.width(10.dp))
                            Column(Modifier.weight(1f)) {
                                Text(art.info.title, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.body2,
                                    color = MaterialTheme.colors.onSurface,
                                    fontWeight = if (active) FontWeight.Medium else FontWeight.Normal)
                                Text(art.artsName, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.caption, color = palette.muted)
                            }
                            if (art.animated) Icon(ArtIcons.Play, null, Modifier.size(12.dp), tint = palette.muted)
                        }
                    }
                }
                ArtsListScrollbar(scrollState, Modifier.align(Alignment.CenterEnd))
            }
        }
        if (artworks.isEmpty()) Spacer(Modifier.weight(1f))
        Divider(color = palette.border)
        Text("${artworks.size} 个作品", Modifier.padding(horizontal = 16.dp, vertical = 12.dp), color = palette.muted, style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun ArtworkInspector(model: ArtsModel, onClose: () -> Unit) {
    val palette = LocalWorkspacePalette.current
    Column(Modifier.fillMaxSize().background(palette.sidebar)) {
        Row(Modifier.fillMaxWidth().height(56.dp).zIndex(2f).padding(start = 16.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("参数", style = MaterialTheme.typography.subtitle2)
            Spacer(Modifier.weight(1f))
            WorkspaceIconButton(ArtIcons.Close, "关闭参数面板", onClose)
        }
        Column(Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)) {
            DynamicForm(model.artsConfig)
            Divider(Modifier.padding(vertical = 24.dp), color = palette.border)
            Text("关于作品", color = palette.muted, style = MaterialTheme.typography.caption)
            Text(model.artsType.info.description, Modifier.padding(top = 8.dp, bottom = 24.dp), color = palette.muted, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
private fun CanvasToolbar(model: ArtsModel) {
    val palette = LocalWorkspacePalette.current
    Row(Modifier.fillMaxWidth().height(72.dp).zIndex(2f).padding(horizontal = 20.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(model.artsType.info.title, maxLines = 1, overflow = TextOverflow.Ellipsis, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
            Text(model.artsType.artsName, maxLines = 1, overflow = TextOverflow.Ellipsis, style = MaterialTheme.typography.caption, color = palette.muted)
        }
        WorkspaceIconButton(ArtIcons.Reset, "重置当前作品", { model.resetCurrent() })
        if (model.artsType.animated) {
            ShadcnButton(onClick = { model.togglePlayback() }) {
                Icon(if (model.isPlaying) ArtIcons.Pause else ArtIcons.Play, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (model.isPlaying) "暂停" else "播放")
            }
        }
    }
}

@Composable
private fun CanvasStage(
    model: ArtsModel, square: Boolean, onSquareChange: (Boolean) -> Unit,
    canvasFocus: FocusRequester, onCanvasSize: (IntSize) -> Unit, modifier: Modifier,
) {
    val palette = LocalWorkspacePalette.current
    var canvasFocused by remember { mutableStateOf(false) }
    Column(modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
        Row(Modifier.fillMaxWidth().height(36.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(6.dp).background(if (model.isPlaying) MaterialTheme.colors.primary else palette.muted, CircleShape))
            Text(if (model.artsType.animated) "动画预览" else "静态预览", Modifier.padding(start = 7.dp), color = palette.muted, style = MaterialTheme.typography.caption)
            Spacer(Modifier.weight(1f))
            val aspectFocus = remember { List(2) { FocusRequester() } }
            var aspectFocusRequest by remember { mutableStateOf<Int?>(null) }
            LaunchedEffect(square, aspectFocusRequest) {
                val index = aspectFocusRequest
                if (index != null && (index == 0) == square) {
                    withFrameNanos { }
                    aspectFocus[index].requestFocus()
                    aspectFocusRequest = null
                }
            }
            Row(Modifier.border(1.dp, palette.border, RoundedCornerShape(6.dp)).padding(2.dp)) {
                listOf(true to "1:1", false to "自适应").forEachIndexed { index, (value, title) ->
                    val interactions = remember { MutableInteractionSource() }
                    val hovered by interactions.collectIsHoveredAsState()
                    val focused by interactions.collectIsFocusedAsState()
                    Box(Modifier.height(28.dp).focusRequester(aspectFocus[index])
                        .focusProperties { canFocus = value == square }.clip(RoundedCornerShape(4.dp))
                        .background(if (value == square || hovered) palette.selection else Color.Transparent)
                        .then(if (focused) Modifier.border(2.dp, palette.ring, RoundedCornerShape(4.dp)) else Modifier)
                        .hoverable(interactions)
                        .onPreviewKeyEvent { event ->
                            val next = if (event.type == KeyEventType.KeyDown &&
                                !event.isCtrlPressed && !event.isMetaPressed && !event.isAltPressed && !event.isShiftPressed) when (event.key) {
                                Key.DirectionLeft, Key.DirectionRight -> 1 - index
                                Key.MoveHome -> 0
                                Key.MoveEnd -> 1
                                else -> null
                            } else null
                            if (next != null) {
                                onSquareChange(next == 0)
                                aspectFocusRequest = next
                                true
                            } else false
                        }
                        .selectable(value == square, interactionSource = interactions, indication = null,
                            role = Role.RadioButton, onClick = { onSquareChange(value) })
                        .padding(horizontal = 10.dp), contentAlignment = Alignment.Center) {
                        Text(title, color = if (value == square) MaterialTheme.colors.onSurface else palette.muted,
                            style = MaterialTheme.typography.caption)
                    }
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        BoxWithConstraints(Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            val frameModifier = if (square) Modifier.size(minOf(maxWidth, maxHeight)) else Modifier.fillMaxSize()
            Box(frameModifier
                .border(1.dp, if (canvasFocused) palette.ring else palette.border, RoundedCornerShape(4.dp))
                .padding(1.dp)
                .focusRequester(canvasFocus)
                .onFocusChanged { canvasFocused = it.isFocused }
                .onPreviewKeyEvent { event ->
                    if (event.key == Key.Spacebar && model.artsType.animated && !event.isCtrlPressed && !event.isMetaPressed && !event.isAltPressed && !event.isShiftPressed) {
                        if (event.type == KeyEventType.KeyUp) model.togglePlayback()
                        true
                    } else false
                }
                .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null) { canvasFocus.requestFocus() }
                .semantics { contentDescription = "${model.artsType.info.title}画布，${if (model.artsType.animated) "空格播放或暂停" else "静态作品"}" },
            ) {
                ArtsView(model, Modifier.fillMaxSize().onSizeChanged(onCanvasSize))
            }
        }
    }
}

@Composable
private fun WorkspaceStatus(model: ArtsModel, canvasSize: IntSize, layout: WorkspaceLayout) {
    val palette = LocalWorkspacePalette.current
    Divider(color = palette.border)
    Row(Modifier.fillMaxWidth().height(28.dp).background(palette.sidebar).padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(when { !model.artsType.animated -> "静态作品"; model.isPlaying -> "动画播放中"; else -> "动画已暂停" }, color = palette.muted, style = MaterialTheme.typography.caption)
        if (layout != WorkspaceLayout.Compact) {
            Text("·", Modifier.padding(horizontal = 12.dp), color = palette.muted)
            Text("Ctrl / ⌘ K 搜索", color = palette.muted, style = MaterialTheme.typography.caption)
        }
        Spacer(Modifier.weight(1f))
        if (canvasSize != IntSize.Zero) Text("${canvasSize.width} × ${canvasSize.height} px", color = palette.muted, style = MaterialTheme.typography.caption)
    }
}

@Composable
private fun WorkspaceIconButton(icon: ImageVector, label: String, onClick: () -> Unit, active: Boolean = false) {
    val interactions = remember { MutableInteractionSource() }
    val hovered by interactions.collectIsHoveredAsState()
    var focused by remember { mutableStateOf(false) }
    var showHint by remember { mutableStateOf(false) }
    LaunchedEffect(hovered, focused) {
        showHint = false
        if (focused) showHint = true
        else if (hovered) {
            delay(500)
            showHint = true
        }
    }
    val windowWidth = LocalWindowInfo.current.containerSize.width
    var alignRight by remember { mutableStateOf(false) }
    Box(Modifier.size(44.dp).hoverable(interactions).zIndex(if (showHint) 1f else 0f)
        .onGloballyPositioned { alignRight = it.positionInRoot().x > windowWidth / 2 }) {
        ShadcnIconButton(icon, label, onClick, active,
            modifier = Modifier.align(Alignment.Center).size(36.dp)
                .onFocusChanged { focused = it.isFocused }
                .onPreviewKeyEvent {
                    if (it.key == Key.Escape && it.type == KeyEventType.KeyDown && showHint) {
                        showHint = false; true
                    } else false
                })
        if (showHint) {
            Surface(
                modifier = Modifier.align(if (alignRight) Alignment.TopEnd else Alignment.TopStart)
                    .offset(y = 44.dp).wrapContentSize(if (alignRight) Alignment.TopEnd else Alignment.TopStart, unbounded = true),
                color = MaterialTheme.colors.onSurface, shape = RoundedCornerShape(6.dp),
            ) {
                Text(label, Modifier.widthIn(max = 260.dp).padding(horizontal = 10.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.caption, color = MaterialTheme.colors.surface)
            }
        }
    }
}

@Composable
private fun PanelDivider() {
    Box(Modifier.width(1.dp).fillMaxHeight().background(LocalWorkspacePalette.current.border))
}
