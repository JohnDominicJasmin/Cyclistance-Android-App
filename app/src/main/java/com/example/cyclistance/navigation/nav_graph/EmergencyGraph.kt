package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.SHOULD_OPEN_CONTACT_DIALOG
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.EmergencyCallScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.emergencyCallGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = Screens.EmergencyCallNavigation.EmergencyCall.screenRoute + "?$SHOULD_OPEN_CONTACT_DIALOG={$SHOULD_OPEN_CONTACT_DIALOG}",
        route = Screens.EmergencyCallNavigation.ROUTE) {
        composable(
            route = Screens.EmergencyCallNavigation.EmergencyCall.screenRoute + "?${SHOULD_OPEN_CONTACT_DIALOG}={${SHOULD_OPEN_CONTACT_DIALOG}}",
            arguments = listOf(navArgument(SHOULD_OPEN_CONTACT_DIALOG) {
                type = NavType.BoolType

                defaultValue = false
            })) { backStackEntry ->

            EmergencyCallScreen(
                navController = navController, paddingValues = paddingValues,
                shouldOpenAddNewContact = backStackEntry.arguments?.getBoolean(
                    SHOULD_OPEN_CONTACT_DIALOG) ?: false
            )
        }


    }
}

