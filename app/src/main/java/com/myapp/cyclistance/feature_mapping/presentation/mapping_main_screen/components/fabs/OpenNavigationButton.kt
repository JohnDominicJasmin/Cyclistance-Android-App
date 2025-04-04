package com.myapp.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme
@Composable
fun OpenNavigationButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    FabFactory(
        modifier = modifier,
        onClick = onClick,
        contentDescription = "Open Navigation App Button",
        iconId = R.drawable.ic_baseline_navigation_24,
    )
}

@Preview(name = "OpenNavigationButton")
@Composable
private fun PreviewOpenNavigationButton() {
    CyclistanceTheme(false) {
        OpenNavigationButton(modifier = Modifier.size(53.dp))
    }
}