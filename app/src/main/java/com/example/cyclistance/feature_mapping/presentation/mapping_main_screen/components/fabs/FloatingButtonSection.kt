package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.fabs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun FloatingButtonSection(
    modifier: Modifier = Modifier,
    locationPermissionGranted: Boolean = true,
    isNavigating: Boolean,
    onClickLocateUserButton: () -> Unit = {},
    onClickRouteOverviewButton: () -> Unit = {},
    onClickRecenterButton: () -> Unit = {},
    onClickOpenNavigationButton: () -> Unit = {}) {

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        MappingUtils.FabAnimated(isNavigating) {
            RouteOverViewButton(
                modifier = Modifier.size(53.dp),
                onClick = onClickRouteOverviewButton
            )
        }

        MappingUtils.FabAnimated(isNavigating) {
            RecenterButton(
                modifier = Modifier.size(53.dp),
                onClick = onClickRecenterButton)
        }

        Box {
            MappingUtils.FabAnimated(visible = isNavigating) {
                OpenNavigationButton(
                    modifier = Modifier.size(53.dp),
                    onClick = onClickOpenNavigationButton
                )
            }
            MappingUtils.FabAnimated(isNavigating.not()) {
                LocateUserButton(
                    modifier = Modifier.size(53.dp),
                    locationPermissionGranted = locationPermissionGranted,
                    onClick = onClickLocateUserButton
                )
            }
        }

    }
}
@Preview
@Composable
fun PreviewFloatingButtons() {
    CyclistanceTheme(true) {
        FloatingButtonSection(isNavigating = true)
    }
}

