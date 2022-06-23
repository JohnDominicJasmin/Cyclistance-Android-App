package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun SettingsButtonItem(buttonText: String, onClick: () -> Unit) {

    SettingsButtonCreator(onClick = onClick) {

        Text(
            text = buttonText,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .weight(0.88f)
                .padding(top = 7.dp, bottom = 7.dp, start = 5.dp))
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Arrow",
            tint = Color.Unspecified)


    }
}



@Preview
@Composable
fun ButtonItemPreview() {

    CyclistanceTheme(true) {
        SettingsButtonItem(buttonText = "Edit Profile") {

        }
    }
}
