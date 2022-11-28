package com.example.cyclistance.feature_settings.presentation.setting_main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.R
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.components.SectionTitle
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.components.SettingSwitchButton
import com.example.cyclistance.feature_settings.presentation.setting_main_screen.components.SettingsButtonItem
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.navigateScreen
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun SettingScreen(
    hasInternetConnection : Boolean,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit,
    paddingValues: PaddingValues,
    navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
                navController.navigateScreen(Screens.EditProfileScreen.route)
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
        val navController = rememberNavController()
        SettingScreen(hasInternetConnection = true, isDarkTheme = true, onToggleTheme = {}, navController = navController, paddingValues = PaddingValues())
    }
}


