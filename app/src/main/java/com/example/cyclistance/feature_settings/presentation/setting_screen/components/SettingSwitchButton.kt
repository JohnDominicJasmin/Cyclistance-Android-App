package com.example.cyclistance.feature_settings.presentation.setting_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingSwitchButton(checkedState: Boolean, onCheckedChange: ((Boolean) -> Unit)?, onClick: () -> Unit) {

    SettingsButtonCreator(onClick = onClick) {

        Text(
            text = "Dark Mode",
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .weight(0.9f)
                .padding(top = 7.dp, bottom = 7.dp, start = 5.dp))

        Spacer(modifier = Modifier.width(10.dp))


        Switch(
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colors.primary,
                uncheckedThumbColor = MaterialTheme.colors.primary),
            checked = checkedState,
            onCheckedChange = onCheckedChange
        )
    }
}


