package com.example.cyclistance.feature_settings.presentation.setting_main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun SettingScreen(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
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
                onCheckedChange = {
                    onToggleTheme()
                }, onClick = {
                    onToggleTheme()
                })
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {




            SectionTitle(iconId = R.drawable.ic_baseline_person_24, title = "Account")
            SettingsButtonItem(buttonText = "Edit Profile", onClick = {
                navigateTo(Screens.EditProfileScreen.route, null)
            })

        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 30.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)) {

            SectionTitle(iconId = R.drawable.ic_baseline_settings_suggest_24, title = "Other Settings")
            SettingsButtonItem(buttonText = "Privacy Policy", onClick = {

            })
            SettingsButtonItem(buttonText = "Terms and Condition", onClick = {

            })
            SettingsButtonItem(buttonText = "Rate this app", onClick = {

            })


        }


    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    CyclistanceTheme(true) {
        SettingScreen(true,onToggleTheme = {}) { _, _ ->

        }
    }
}


