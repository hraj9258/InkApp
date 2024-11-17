package com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.ink.brush.Brush
import com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker.ColorSpectrum
import com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker.ColorUI
import com.hraj9258.inkapp.presentation.drawing_screen.components.toolbar_components.color_picker.ColorUIList
import com.hraj9258.inkapp.ui.theme.InkAppTheme
import com.hraj9258.inkapp.ui.theme.latteBlue
import com.hraj9258.inkapp.ui.theme.latteFlamingo
import com.hraj9258.inkapp.ui.theme.latteGreen
import com.hraj9258.inkapp.ui.theme.latteLavender
import com.hraj9258.inkapp.ui.theme.latteMaroon
import com.hraj9258.inkapp.ui.theme.latteMauve
import com.hraj9258.inkapp.ui.theme.lattePeach
import com.hraj9258.inkapp.ui.theme.lattePink
import com.hraj9258.inkapp.ui.theme.latteRed
import com.hraj9258.inkapp.ui.theme.latteRosewater
import com.hraj9258.inkapp.ui.theme.latteSapphire
import com.hraj9258.inkapp.ui.theme.latteSky
import com.hraj9258.inkapp.ui.theme.latteTeal
import com.hraj9258.inkapp.ui.theme.latteYellow
import com.hraj9258.inkapp.ui.theme.mochaBlue
import com.hraj9258.inkapp.ui.theme.mochaFlamingo
import com.hraj9258.inkapp.ui.theme.mochaGreen
import com.hraj9258.inkapp.ui.theme.mochaLavender
import com.hraj9258.inkapp.ui.theme.mochaMaroon
import com.hraj9258.inkapp.ui.theme.mochaMauve
import com.hraj9258.inkapp.ui.theme.mochaPeach
import com.hraj9258.inkapp.ui.theme.mochaPink
import com.hraj9258.inkapp.ui.theme.mochaRed
import com.hraj9258.inkapp.ui.theme.mochaRosewater
import com.hraj9258.inkapp.ui.theme.mochaSapphire
import com.hraj9258.inkapp.ui.theme.mochaSky
import com.hraj9258.inkapp.ui.theme.mochaTeal
import com.hraj9258.inkapp.ui.theme.mochaYellow

val standardColorList = listOf(
    Color.Black,
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Magenta,
    Color.Cyan,
    Color.White
)

val mochaColorList = listOf(
    mochaRosewater,
    mochaFlamingo,
    mochaPink,
    mochaMauve,
    mochaRed,
    mochaMaroon,
    mochaPeach,
    mochaYellow,
    mochaGreen,
    mochaTeal,
    mochaBlue,
    mochaSapphire,
    mochaSky,
    mochaLavender
)

val latteColorList = listOf(
    latteRosewater,
    latteFlamingo,
    lattePink,
    latteMauve,
    latteRed,
    latteMaroon,
    lattePeach,
    latteYellow,
    latteGreen,
    latteTeal,
    latteBlue,
    latteSapphire,
    latteSky,
    latteLavender
)

@Composable
fun StandardColorUIList(
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Standard Colors",
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .padding(16.dp, 0.dp)
    )
    ColorUIList(
        selectedBrush = selectedBrush,
        colorUIList = standardColorList
    )
}
@Composable
fun MochaColorUIList(
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Catppuccin Mocha Colors",
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .padding(16.dp, 0.dp)
    )
    ColorUIList(
        selectedBrush = selectedBrush,
        colorUIList = mochaColorList
    )
}@Composable
fun LatteColorUIList(
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    Text(
        text = "Catppuccin Latte Colors",
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier
            .padding(16.dp, 0.dp)
    )
    ColorUIList(
        selectedBrush = selectedBrush,
        colorUIList = latteColorList
    )
}

@Composable
fun ColorPicker(
    selectedBrush: MutableState<Brush>,
    modifier: Modifier = Modifier
) {
    val selectedColor = remember { mutableIntStateOf(selectedBrush.value.colorIntArgb) }
    // Update selectedColor when selectedBrush changes
    LaunchedEffect(selectedBrush.value.colorIntArgb) {
        selectedColor.intValue = selectedBrush.value.colorIntArgb
    }
    val expanded = remember { mutableStateOf(false) }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ColorUI(Color(selectedColor.intValue.toULong() shl 32))
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
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    StandardColorUIList(selectedBrush)
                    MochaColorUIList(selectedBrush)
                    LatteColorUIList(selectedBrush)
                    ColorSpectrum()
                }
            }
        }
    }
}

@Preview(widthDp = 100)
@Composable
private fun ColorPickerPreview() {
    InkAppTheme {
        ColorPicker(
            selectedBrush = selectedBrushPreview,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

internal val selectedBrushPreview = mutableStateOf(brushListForPreview.first())