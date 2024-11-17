package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// ToDo: Yet to implement
@Composable
fun ColorSpectrum(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .padding(16.dp, 8.dp)
            .size(200.dp, 150.dp)
            .background(Color(0xFFD0A8C5), shape = CircleShape)
    ) {
        val color = Color(0xFFD0A8C5)
        drawRoundRect(
            color = color,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(16f, 16f)
        )
    }
}