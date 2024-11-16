package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush
import kotlin.math.roundToInt

@Composable
fun SizeSlider(
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedBrushSize = remember { mutableFloatStateOf(selectedBrush.value.size) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Brush Size: ${selectedBrush.value.size}",
            color = MaterialTheme.colorScheme.onSurface
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
                onDismissRequest = { expanded.value = false },
            ) {
                Slider(
                    value = selectedBrushSize.floatValue,
                    onValueChange = { newSize ->
                        val roundedSize = newSize.roundToInt().toFloat()
                        selectedBrushSize.floatValue = roundedSize
                        selectedBrush.value = selectedBrush.value.copy(size = roundedSize)
                    },
                    valueRange = 1f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary
                    ),
                    steps = 100,
                    modifier = Modifier
                        .size(width = 212.dp, height = 50.dp)
                        .padding(horizontal = 16.dp)
                )
            }
        }
    }
}
