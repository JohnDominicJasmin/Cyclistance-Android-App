package com.example.cyclistance.feature_settings.presentation.setting_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_settings.presentation.setting_screen.components.SettingScreenContent
import com.example.cyclistance.navigation.Screens

@Composable
fun SettingScreen(

    onToggleTheme: () -> Unit,
    paddingValues: PaddingValues,
    navController: NavController) {


    SettingScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        onToggleTheme = onToggleTheme,
        onClickEditProfile = {
            navController.navigate(Screens.SettingsNavigation.EditProfileScreen.screenRoute)
        }
    )

}
