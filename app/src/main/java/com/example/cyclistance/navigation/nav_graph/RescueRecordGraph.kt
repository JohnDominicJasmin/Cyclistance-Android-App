package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.RescueDetailsScreen
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.RescueResultsScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.rescueRecordGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    composable(route = Screens.RescueRecordNavigation.RescueResults.screenRoute){
        RescueResultsScreen(
            paddingValues = paddingValues,
            navController = navController
        )
    }

    composable(route = Screens.RescueRecordNavigation.RescueDetails.screenRoute){
        RescueDetailsScreen(paddingValues = paddingValues, navController = navController)
    }
}