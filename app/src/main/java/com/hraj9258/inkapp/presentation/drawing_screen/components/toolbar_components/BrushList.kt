package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush
import androidx.ink.brush.StockBrushes

@Composable
fun BrushList(
    brushList: List<Brush>,
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Brush,
            contentDescription = "Selected Brush",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
        ) {
            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = { expanded.value = true },
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false }
            ) {
                brushList.forEachIndexed { index, brush ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text =
                                if (index == 0) {
                                    "Pressure Brush"
                                } else if (index == 1) {
                                    "Highlighter"
                                } else "Marker Brush"
                            )
                        },
                        onClick = {
                            selectedBrush.value = brush
                            expanded.value = false
                        }
                    )
                }
            }
        }
    }
}

internal val brushListForPreview = listOf(
    Brush.createWithColorIntArgb(
        family = StockBrushes.pressurePenLatest,
        colorIntArgb = 0xFF000000.toInt(),
        size = 5F,
        epsilon = 0.1F
    ),
    Brush.createWithColorIntArgb(
        family = StockBrushes.highlighterLatest,
        colorIntArgb = 0xFF0000FF.toInt(),
        size = 5F,
        epsilon = 0.1F
    ),
    Brush.createWithColorIntArgb(
        family = StockBrushes.markerLatest,
        colorIntArgb = 0xFF00FF00.toInt(),
        size = 5F,
        epsilon = 0.1F
    )
)

