package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.EDIT_CONTACT_ID
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.AddEditContactScreen
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.EmergencyCallScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.emergencyCallGraph(
    navController: NavController,
    paddingValues: PaddingValues,
    changeEditMode : (isOnEditMode: Boolean) -> Unit
) {
    navigation(
        startDestination = Screens.EmergencyCallNavigation.EmergencyCall.screenRoute,
        route = Screens.EmergencyCallNavigation.ROUTE) {

        composable(
            route = Screens.EmergencyCallNavigation.AddEditEmergencyContact.screenRoute, arguments =
            listOf(
                navArgument(EDIT_CONTACT_ID){
                   type = NavType.IntType
                    defaultValue = 0
                })) {

            AddEditContactScreen(navController = navController, paddingValues = paddingValues, changeEditMode = changeEditMode)
        }
        composable(
            route = Screens.EmergencyCallNavigation.EmergencyCall.screenRoute) {

            EmergencyCallScreen(
                navController = navController, paddingValues = paddingValues)
        }



    }
}

