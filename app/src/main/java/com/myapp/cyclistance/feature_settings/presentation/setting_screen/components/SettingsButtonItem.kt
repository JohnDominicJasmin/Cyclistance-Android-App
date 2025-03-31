package com.myapp.cyclistance.feature_settings.presentation.setting_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.R
import com.myapp.cyclistance.theme.CyclistanceTheme


@Composable
fun SettingsButtonItem(buttonText: String, onClick: () -> Unit) {

    SettingsButtonCreator(onClick = onClick) {

        Text(
            text = buttonText,
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.subtitle2.copy(fontWeight = FontWeight.Normal),
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
