package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_ID
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_NAME
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_PHOTO
import com.example.cyclistance.feature_report_account.presentation.ReportAccountScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.onReportAccountGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {

    navigation(
        startDestination = Screens.ReportAccountNavigation.ReportAccount.screenRoute,
        route = Screens.ReportAccountNavigation.ROUTE){

        composable(route = Screens.ReportAccountNavigation.ReportAccount.screenRoute, arguments = listOf(
            navArgument(USER_ID){
                this.type = NavType.StringType
            },
            navArgument(USER_PHOTO){
                this.type = NavType.StringType
            },
            navArgument(USER_NAME){
                this.type = NavType.StringType
            }

        )){
            ReportAccountScreen(
                navController = navController,
                paddingValues = paddingValues
            )
        }

    }



}