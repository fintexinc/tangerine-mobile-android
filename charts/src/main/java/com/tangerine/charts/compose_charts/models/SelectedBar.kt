package com.tangerine.charts.compose_charts.models

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

data class SelectedBar(
    val bar: Bars.Data,
    val offset: Offset,
    val rect: Rect,
    val dataIndex: Int,
    val valueIndex: Int
)

data class SelectedBars(
    val bars: Pair<Bars.Data, Bars.Data>,
    val offset: Offset,
    val rect: Rect,
    val dataIndexes: Pair<Int, Int>,
    val valueIndexes: Pair<Int, Int>
)