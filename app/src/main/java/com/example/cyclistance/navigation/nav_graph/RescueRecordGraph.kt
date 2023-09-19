package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cyclistance.core.utils.constants.UserProfileConstants
import com.example.cyclistance.feature_rescue_record.presentation.rescue_details.RescueDetailsScreen
import com.example.cyclistance.feature_rescue_record.presentation.rescue_results.RescueResultsScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.rescueRecordGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
) {
    composable(route = Screens.RescueRecordNavigation.RescueResults.screenRoute, arguments =
    listOf(
        navArgument(UserProfileConstants.USER_ID){
            this.type = NavType.StringType
        },
        navArgument(UserProfileConstants.USER_PHOTO){
            this.type = NavType.StringType
        },
        navArgument(UserProfileConstants.USER_NAME){
            this.type = NavType.StringType
        }

    )){
        RescueResultsScreen(
            paddingValues = paddingValues,
            navController = navController
        )
    }

    composable(route = Screens.RescueRecordNavigation.RescueDetails.screenRoute){
        RescueDetailsScreen(paddingValues = paddingValues, navController = navController)
    }
}