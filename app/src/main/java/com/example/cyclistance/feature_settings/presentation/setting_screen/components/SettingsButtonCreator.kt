package com.example.cyclistance.feature_settings.presentation.setting_screen.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsButtonCreator(onClick: () -> Unit, content: @Composable RowScope.() -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 3.dp)
            .wrapContentSize(),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.background),
        onClick = onClick,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp,
            pressedElevation = 3.dp),
        content = content
    )
}