package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DarkModeSwitchButton(checkedState: Boolean,onCheckedChange: ((Boolean) -> Unit)?) {


    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(start = 3.dp)
            .wrapContentSize()
            .clickable {
            },
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = "Dark Mode",
            color = Color.White,
            modifier = Modifier
                .weight(0.9f)
                .padding(top = 7.dp, bottom = 7.dp))

        Switch(colors = SwitchDefaults.colors(checkedThumbColor = MaterialTheme.colors.primary, uncheckedThumbColor = MaterialTheme.colors.primary,    ),
            checked = checkedState,
            onCheckedChange = onCheckedChange
        )
    }
}


