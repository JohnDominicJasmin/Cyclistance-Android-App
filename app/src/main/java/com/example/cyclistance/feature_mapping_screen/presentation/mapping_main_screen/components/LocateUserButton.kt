package com.example.cyclistance.feature_mapping_screen.presentation.mapping_main_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.Red700

@Composable
fun LocateUserButton(
    modifier: Modifier = Modifier,
    locationPermissionGranted: Boolean,
    onClick: () -> Unit
) {
    val iconId = remember(locationPermissionGranted){
        if(locationPermissionGranted) R.drawable.ic_baseline_my_location_24 else R.drawable.ic_locate_user_position
    }
    Button(
        modifier = modifier,
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
        ), onClick = onClick) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Locate Button",
            tint = if(locationPermissionGranted)  MaterialTheme.colors.onSurface else Red700
        )
    }
}

