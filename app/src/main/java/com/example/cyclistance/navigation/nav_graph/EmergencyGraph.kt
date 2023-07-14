package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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


    }
}

