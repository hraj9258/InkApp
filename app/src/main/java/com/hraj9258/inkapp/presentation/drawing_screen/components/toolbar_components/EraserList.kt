package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush

@Composable
fun EraserList(
    eraserList: List<Brush>,
    modifier: Modifier = Modifier
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedEraser = remember { mutableStateOf(Brush) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "Eraser", // ToDo: Implement the eraser brush
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
                onDismissRequest = { expanded.value = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Yet To Implement",
                        )
                    },
                    onClick = {
                        expanded.value = false
                    },
                    enabled = false
                )
            }
        }
    }
}