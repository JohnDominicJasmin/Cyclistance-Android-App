package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun MapLayerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {


    FabFactory(
        modifier = modifier,
        iconId = R.drawable.baseline_layers_24,
        onClick = onClick,
        contentDescription = "Map Layer Button"
    )
}

@Preview
@Composable
fun PreviewMapLayerButtonDark() {
    CyclistanceTheme(darkTheme = true) {
        MapLayerButton(modifier = Modifier.size(53.dp))
    }
}

@Preview
@Composable
fun PreviewMapLayerButtonLight() {
    CyclistanceTheme(darkTheme = false) {
        MapLayerButton(modifier = Modifier.size(53.dp))
    }
}