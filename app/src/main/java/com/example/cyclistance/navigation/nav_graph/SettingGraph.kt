package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cyclistance.feature_settings.presentation.setting_change_password.ChangePasswordScreen
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileScreen
import com.example.cyclistance.feature_settings.presentation.setting_screen.SettingScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.settingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onToggleTheme: () -> Unit) {
    navigation(
        startDestination = Screens.SettingsNavigation.SettingScreen.screenRoute,
        route = Screens.SettingsNavigation.ROUTE) {

        composable(Screens.SettingsNavigation.ChangePasswordScreen.screenRoute) {
            ChangePasswordScreen(paddingValues = paddingValues)
        }

        composable(Screens.SettingsNavigation.EditProfileScreen.screenRoute) {
            EditProfileScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(Screens.SettingsNavigation.SettingScreen.screenRoute) {
            SettingScreen(
                onToggleTheme = onToggleTheme,
                navController = navController,
                paddingValues = paddingValues)

        }
    }

}

