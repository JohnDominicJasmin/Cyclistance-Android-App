package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun RouteOverViewButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    FabFactory(modifier = modifier,
        iconId = com.mapbox.navigation.R.drawable.mapbox_ic_route_overview,
        onClick = onClick,
        contentDescription = "Route Line Button")
}

@Preview(name = "RouteLineButton")
@Composable
private fun PreviewRouteLineButton() {
    CyclistanceTheme(true) {
        RouteOverViewButton(modifier = Modifier.size(53.dp))
    }
}