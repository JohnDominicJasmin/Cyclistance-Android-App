package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RecenterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    FabFactory(modifier = modifier,
        iconId = R.drawable.ic_recenter,
        onClick = onClick,
        contentDescription = "Recenter Button")
}

@Preview(name = "RecenterButton")
@Composable
private fun PreviewRecenterButton() {
    CyclistanceTheme(true) {
        RecenterButton(modifier = Modifier.size(53.dp),)
    }
}