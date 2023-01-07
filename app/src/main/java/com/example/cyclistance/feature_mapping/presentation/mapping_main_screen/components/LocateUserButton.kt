package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.theme.Red700

@Composable
fun LocateUserButton(
    modifier: Modifier = Modifier,
    locationPermissionGranted: Boolean,
    onClick: () -> Unit
) {
    val iconId = remember(locationPermissionGranted) {
        if (locationPermissionGranted) R.drawable.ic_baseline_my_location_24 else R.drawable.ic_locate_user_position
    }

    FabFactory(
        modifier = modifier,
        iconId = iconId,
        onClick = onClick,
        contentDescription = "Locate User Button",
        tint = if (locationPermissionGranted) MaterialTheme.colors.onSurface else Red700)

}

@Preview
@Composable
fun PreviewLocateUserButton() {
    CyclistanceTheme(true) {
        LocateUserButton(
            locationPermissionGranted = true,
            onClick = {},
            modifier = Modifier.size(53.dp))
    }
}

