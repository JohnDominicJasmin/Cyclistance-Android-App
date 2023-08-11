package com.example.cyclistance.feature_settings.presentation.setting_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.IsDarkTheme
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun SettingScreenContent(
    modifier: Modifier = Modifier,
    onToggleTheme: () -> Unit = {},
    onClickEditProfile: () -> Unit = {},
    onClickChangePassword: () -> Unit = {},
    ) {

    val isDarkTheme = IsDarkTheme.current
    Surface(modifier = modifier, color = MaterialTheme.colors.background) {

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)) {

                SectionTitle(iconId = R.drawable.ic_baseline_brush_24, title = "Preference")
                SettingSwitchButton(
                    checkedState = isDarkTheme,
                    onCheckedChange = { onToggleTheme() },
                    onClick = onToggleTheme)


            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)) {


                SectionTitle(iconId = R.drawable.ic_baseline_person_24, title = "Account")
                SettingsButtonItem(buttonText = "Edit Profile", onClick = onClickEditProfile)

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)) {


                SectionTitle(iconId = R.drawable.baseline_lock_24, title = "Security")
                SettingsButtonItem(buttonText = "Change Password", onClick = onClickChangePassword)

            }


            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 30.dp),
                verticalArrangement = Arrangement.spacedBy(3.dp)) {

                SectionTitle(
                    iconId = R.drawable.ic_baseline_settings_suggest_24,
                    title = "Other Settings")

                SettingsButtonItem(buttonText = "Privacy Policy", onClick = {

                })
                SettingsButtonItem(buttonText = "Rate this app", onClick = {

                })


            }
        }

    }
}

@Preview
@Composable
fun PreviewSettingScreenDark() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(true) {

            SettingScreenContent(
                onToggleTheme = {},
            )
        }
    }
}


@Preview
@Composable
fun PreviewSettingScreenLight() {
    CompositionLocalProvider(IsDarkTheme provides true) {
        CyclistanceTheme(false) {

            SettingScreenContent(
                onToggleTheme = {},
            )
        }
    }
}
