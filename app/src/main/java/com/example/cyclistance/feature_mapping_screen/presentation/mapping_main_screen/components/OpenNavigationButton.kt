package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun OpenNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FabFactory(
        modifier = modifier,
        onClick = onClick,
        contentDescription = "Open Navigation App Button",
        iconId = com.mapbox.navigation.R.drawable.mapbox_ic_recenter,
    )
}

@Preview(name = "OpenNavigationButton")
@Composable
private fun PreviewOpenNavigationButton() {
    CyclistanceTheme(false) {
        OpenNavigationButton(modifier = Modifier.size(53.dp))
    }
}