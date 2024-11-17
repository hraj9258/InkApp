package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush

@Composable
fun ColorUI(
    color: Color,
    selectedBrush: MutableState<Brush>? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSurface)
            .clickable{
                if (selectedBrush != null){
                    selectedBrush.value = Brush.createWithColorIntArgb(
                        family = selectedBrush.value.family,
                        colorIntArgb = color.toArgb(),
                        size = selectedBrush.value.size,
                        epsilon = selectedBrush.value.epsilon
                    )
                }
            }
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color)
                .align(Alignment.Center)
        )
    }
}