package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.MappingState
import com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.utils.MappingUtils
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun FloatingButtonSection(
    state: MappingState = MappingState(),
    modifier: Modifier = Modifier,
    locationPermissionGranted: Boolean = true,
    onClickLocateUserButton: () -> Unit = {},
    onClickRouteOverButton: () -> Unit = {},
    onClickRecenterButton: () -> Unit = {},
    onClickOpenNavigationButton: () -> Unit = {}) {

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        MappingUtils.FabAnimated(state.isNavigating) {
            RouteOverViewButton(
                modifier = Modifier.size(53.dp),
                onClick = onClickRouteOverButton
            )
        }

        MappingUtils.FabAnimated(state.isNavigating) {
            RecenterButton(
                modifier = Modifier.size(53.dp),
                onClick = onClickRecenterButton)
        }

        Box {
            MappingUtils.FabAnimated(visible = state.isNavigating) {
                OpenNavigationButton(
                    modifier = Modifier.size(53.dp),
                    onClick = onClickOpenNavigationButton
                )
            }
            MappingUtils.FabAnimated(state.isNavigating.not()) {
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
        FloatingButtonSection(state = MappingState(
            isNavigating = true,
        ))
    }
}

