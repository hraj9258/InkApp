package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorUIList(
    selectedBrush: MutableState<Brush>,
    colorUIList: List<Color>,
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier
            .padding(16.dp, 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        maxItemsInEachRow = 7
    ) {
        colorUIList.forEachIndexed { index, color ->
            ColorUI(color, selectedBrush)
        }
    }
}