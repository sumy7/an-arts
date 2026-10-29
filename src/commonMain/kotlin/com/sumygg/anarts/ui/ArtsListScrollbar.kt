package com.sumygg.anarts.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ArtsListScrollbar(state: LazyListState, modifier: Modifier)
