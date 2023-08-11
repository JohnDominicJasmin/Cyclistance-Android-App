package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.cyclistance.core.utils.constants.MappingConstants.SELECTION_RESCUEE_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CANCELLATION_TYPE
import com.example.cyclistance.core.utils.constants.NavigationConstants.CLIENT_ID
import com.example.cyclistance.core.utils.constants.NavigationConstants.LATITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.LONGITUDE
import com.example.cyclistance.core.utils.constants.NavigationConstants.TRANSACTION_ID
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
        startDestination = Screens.MappingNavigation.Mapping.screenRoute,
        route = Screens.MappingNavigation.ROUTE) {

        composable(route = Screens.MappingNavigation.Mapping.screenRoute) {
            MappingScreen(
                hasInternetConnection = hasInternetConnection,
                navController = navController,
                paddingValues = paddingValues,
                isNavigating = isNavigating,
                onChangeNavigatingState = onChangeNavigatingState
            )
        }

        composable(
            route = "${Screens.MappingNavigation.Cancellation.screenRoute}/{$CANCELLATION_TYPE}/{$TRANSACTION_ID}/{$CLIENT_ID}",
            arguments = listOf(
                navArgument(CANCELLATION_TYPE) {
                    defaultValue = SELECTION_RESCUEE_TYPE
                },
                navArgument(TRANSACTION_ID) {},
                navArgument(CLIENT_ID) {})) {

            it.arguments?.getString(CANCELLATION_TYPE)
                ?.let { cancellationType ->
                    CancellationReasonScreen(
                        navController = navController,
                        paddingValues = paddingValues,
                        cancellationType = cancellationType)
                }
        }


        composable(route = Screens.MappingNavigation.ConfirmDetails.screenRoute + "?${LATITUDE}={${LATITUDE}}&${LONGITUDE}={${LONGITUDE}}",
            arguments = listOf(
                navArgument(LATITUDE) {
                    type = NavType.FloatType; defaultValue = -1f
                },
                navArgument(LONGITUDE) {
                    type = NavType.FloatType; defaultValue = -1f
                }
            )) {

            ConfirmDetailsScreen(
                navController = navController,
                paddingValues = paddingValues)

        }

    }
}
