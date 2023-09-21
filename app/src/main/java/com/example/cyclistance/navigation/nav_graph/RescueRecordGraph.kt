package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.RescueRecordConstants
import com.example.cyclistance.feature_rescue_record.presentation.list_histories.presentation.RideHistoryScreen
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.RescueDetailsScreen
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.RescueResultsScreen
import com.example.cyclistance.feature_rescue_record.presentation.ride_history_details.RideHistoryDetailsScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.rescueRecordGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {


    navigation(
        startDestination = Screens.RescueRecordNavigation.RescueResults.screenRoute,
        route = Screens.RescueRecordNavigation.ROUTE) {

        composable(route = Screens.RescueRecordNavigation.RescueResults.screenRoute) {
            RescueResultsScreen(
                paddingValues = paddingValues,
                navController = navController
            )
        }

        composable(route = Screens.RescueRecordNavigation.RescueDetails.screenRoute, arguments =
        listOf(
            navArgument(name = RescueRecordConstants.TRANSACTION_ID) {
                this.type = NavType.StringType
            })) {
            it.arguments?.getString(RescueRecordConstants.TRANSACTION_ID)?.let { transactionId ->
                RescueDetailsScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    transactionId = transactionId)
            }
        }


        composable(route = Screens.RescueRecordNavigation.RideHistory.screenRoute) {
            RideHistoryScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(route = Screens.RescueRecordNavigation.RideHistoryDetails.screenRoute) {
            RideHistoryDetailsScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }


    }

}