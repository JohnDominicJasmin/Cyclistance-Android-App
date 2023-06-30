package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MappingConstants
import com.example.cyclistance.core.utils.constants.NavigationConstants
import com.example.cyclistance.feature_mapping.presentation.mapping_cancellation_reason.CancellationReasonScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.ConfirmDetailsScreen
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.MappingScreen
import com.example.cyclistance.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.mappingGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    hasInternetConnection: Boolean,
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit) {

    navigation(
        startDestination = Screens.Mapping.MappingScreen.screenRoute,
        route = Screens.Mapping.ROUTE) {

        composable(route = Screens.Mapping.MappingScreen.screenRoute) {
            MappingScreen(
                hasInternetConnection = hasInternetConnection,
                navController = navController,
                paddingValues = paddingValues,
                isNavigating = isNavigating,
                onChangeNavigatingState = onChangeNavigatingState
            )
        }

        composable(
            route = "${Screens.Mapping.CancellationScreen.screenRoute}/{${NavigationConstants.CANCELLATION_TYPE}}/{${NavigationConstants.TRANSACTION_ID}}/{${NavigationConstants.CLIENT_ID}}",
            arguments = listOf(
                navArgument(NavigationConstants.CANCELLATION_TYPE) {
                    defaultValue = MappingConstants.SELECTION_RESCUEE_TYPE
                },
                navArgument(NavigationConstants.TRANSACTION_ID) {},
                navArgument(NavigationConstants.CLIENT_ID) {})) {

            it.arguments?.getString(NavigationConstants.CANCELLATION_TYPE)
                ?.let { cancellationType ->
                    CancellationReasonScreen(
                        navController = navController,
                        paddingValues = paddingValues,
                        cancellationType = cancellationType)
                }
        }


        composable(route = Screens.Mapping.ConfirmDetailsScreen.screenRoute + "?${NavigationConstants.LATITUDE}={${NavigationConstants.LATITUDE}}&${NavigationConstants.LONGITUDE}={${NavigationConstants.LONGITUDE}}",
            arguments = listOf(
                navArgument(NavigationConstants.LATITUDE) {
                    type = NavType.FloatType; defaultValue = -1f
                },
                navArgument(NavigationConstants.LONGITUDE) {
                    type = NavType.FloatType; defaultValue = -1f
                }
            )) {

            ConfirmDetailsScreen(
                navController = navController,
                paddingValues = paddingValues)

        }

    }
}
