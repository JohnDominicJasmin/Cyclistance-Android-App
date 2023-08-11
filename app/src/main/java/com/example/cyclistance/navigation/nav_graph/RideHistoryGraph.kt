package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.cyclistance.feature_ride_history.presentation.list_histories.presentation.RideHistoryScreen
import com.example.cyclistance.feature_ride_history.presentation.ride_history_details.RideHistoryDetailsScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.rideHistoryGraph(
    navController: NavHostController,
    paddingValues: PaddingValues
) {

    navigation(
        startDestination = Screens.RideHistoryNavigation.RideHistory.screenRoute,
        route = Screens.RideHistoryNavigation.ROUTE) {


        composable(route = Screens.RideHistoryNavigation.RideHistory.screenRoute) {
            RideHistoryScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

        composable(route = Screens.RideHistoryNavigation.RideHistoryDetails.screenRoute) {
            RideHistoryDetailsScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

    }

}