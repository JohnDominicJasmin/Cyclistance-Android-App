package com.myapp.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.myapp.cyclistance.core.utils.constants.RescueRecordConstants
import com.myapp.cyclistance.feature_rescue_record.presentation.history_details.HistoryDetailsScreen
import com.myapp.cyclistance.feature_rescue_record.presentation.list_histories.presentation.RideHistoryScreen
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_details.RescueDetailsScreen
import com.myapp.cyclistance.feature_rescue_record.presentation.rescue_results.RescueResultsScreen
import com.myapp.cyclistance.navigation.Screens

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
                nullable = true
                defaultValue = null
            })) {

                RescueDetailsScreen(
                    paddingValues = paddingValues,
                    navController = navController)

        }


        composable(route = Screens.RescueRecordNavigation.RideHistory.screenRoute, arguments = listOf(
            navArgument(name = RescueRecordConstants.RIDE_HISTORY_UID) {
                this.type = NavType.StringType
            }
        )) {
            RideHistoryScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(route = Screens.RescueRecordNavigation.RideHistoryDetails.screenRoute, arguments =
        listOf(
            navArgument(name = RescueRecordConstants.TRANSACTION_ID) {
                this.type = NavType.StringType
            })) {
            it.arguments?.getString(RescueRecordConstants.TRANSACTION_ID)?.let { transactionId ->
                HistoryDetailsScreen(
                    navController = navController,
                    paddingValues = paddingValues,
                    transactionId = transactionId

                )
            }
        }


    }

}