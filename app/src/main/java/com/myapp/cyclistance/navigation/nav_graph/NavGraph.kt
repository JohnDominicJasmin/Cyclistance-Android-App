package com.myapp.cyclistance.navigation.nav_graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.myapp.cyclistance.core.network.ConnectivityObserver
import com.myapp.cyclistance.navigation.event.NavUiEvent
import com.myapp.cyclistance.navigation.state.NavUiState


@ExperimentalPermissionsApi
@Composable
fun NavGraph(
    navController: NavHostController,
    paddingValues: PaddingValues,
    uiState: NavUiState,
    event: (NavUiEvent) -> Unit) {


    NavHost(
        navController = navController,
        startDestination = uiState.startingDestination,
        enterTransition = {
            fadeIn(
                animationSpec = tween(
                    durationMillis = 300))
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(
                    durationMillis = 300))
        }) {


        authenticationGraph(
            navController = navController,
            paddingValues = paddingValues,
        )

        mappingGraph(
            navController = navController,
            paddingValues = paddingValues,
            hasInternetConnection = uiState.internetStatus == ConnectivityObserver.Status.Available,
            isNavigating = uiState.isNavigating,
            onChangeNavigatingState = { event(NavUiEvent.OnChangeNavigation(it)) }
        )

        emergencyCallGraph(
            navController = navController,
            paddingValues = paddingValues,
        )

        messagingGraph(
            navController = navController,
            paddingValues = paddingValues,
            isInternetAvailable = uiState.internetStatus == ConnectivityObserver.Status.Available,
        )

        onBoardingGraph(
            navController = navController,
            paddingValues = paddingValues
        )

        settingGraph(
            navController = navController,
            paddingValues = paddingValues,
            onToggleTheme = { event(NavUiEvent.OnToggleTheme) }
        )


        userProfileGraph(
            navController = navController,
            paddingValues = paddingValues
        )

        onReportAccountGraph(
            navController = navController,
            paddingValues = paddingValues
        )
        rescueRecordGraph(
            navController = navController,
            paddingValues = paddingValues
        )

    }
}


fun NavController.navigateScreenInclusively(
    destination: String,
    popUpToDestination: String) {
    navigate(destination) {
        popUpTo(popUpToDestination) {
            inclusive = true
        }
        launchSingleTop = true
    }

}

    fun NavController.navigateScreen(
        route: String) {
        navigate(route) {
            popUpTo(route) {
                saveState = true

            }
            restoreState = true
            launchSingleTop = true
        }
    }
