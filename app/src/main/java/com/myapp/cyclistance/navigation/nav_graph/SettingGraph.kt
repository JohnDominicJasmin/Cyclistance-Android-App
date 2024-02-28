package com.myapp.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.myapp.cyclistance.feature_settings.presentation.setting_screen.SettingScreen
import com.myapp.cyclistance.navigation.Screens

fun NavGraphBuilder.settingGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    onToggleTheme: () -> Unit) {
    navigation(
        startDestination = Screens.SettingsNavigation.Setting.screenRoute,
        route = Screens.SettingsNavigation.ROUTE) {


        composable(Screens.SettingsNavigation.Setting.screenRoute) {
            SettingScreen(
                onToggleTheme = onToggleTheme,
                navController = navController,
                paddingValues = paddingValues)

        }
    }

}

