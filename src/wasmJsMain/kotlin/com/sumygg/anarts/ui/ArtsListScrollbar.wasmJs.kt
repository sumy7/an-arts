package com.sumygg.anarts.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// Browser scrolling uses the shared LazyColumn's wheel and touch handling.
@Composable
actual fun ArtsListScrollbar(state: LazyListState, modifier: Modifier) = Unit
