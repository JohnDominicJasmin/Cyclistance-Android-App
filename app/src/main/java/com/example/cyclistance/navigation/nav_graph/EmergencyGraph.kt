package com.example.cyclistance.navigation.nav_graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.EmergencyAddEditContactScreen
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.EmergencyCallScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.emergencyCallGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = Screens.EmergencyCall.EmergencyCallScreen.screenRoute,
        route = Screens.EmergencyCall.ROUTE) {

        composable(
            route = Screens.EmergencyCall.EmergencyCallScreen.screenRoute) {

            EmergencyCallScreen(
                navController = navController, paddingValues = paddingValues
            )
        }

        composable(Screens.EmergencyCall.AddNewContact.screenRoute + "?contactId={contactId}",
            arguments = listOf(
                navArgument("contactId") {
                    nullable = true
                }), enterTransition = {
                fadeIn(
                    animationSpec = tween(
                        durationMillis = 1000))
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(
                        durationMillis = 1000))
            }) {

            EmergencyAddEditContactScreen(
                navController = navController, paddingValues = paddingValues
            )
        }

    }
}

