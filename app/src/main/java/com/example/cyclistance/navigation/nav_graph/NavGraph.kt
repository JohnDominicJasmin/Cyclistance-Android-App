package com.example.cyclistance.navigation.nav_graph

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.accompanist.permissions.ExperimentalPermissionsApi


@ExperimentalPermissionsApi
@Composable
fun NavGraph(
    hasInternetConnection: Boolean,
    navController: NavHostController,
    paddingValues: PaddingValues,
    startingDestination: String,
    isNavigating: Boolean,
    onChangeNavigatingState: (isNavigating: Boolean) -> Unit,
    onToggleTheme: () -> Unit) {

    NavHost(
        navController = navController,
        startDestination = startingDestination,
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
            hasInternetConnection = hasInternetConnection,
            isNavigating = isNavigating,
            onChangeNavigatingState = onChangeNavigatingState
        )

        emergencyCallGraph(
            navController = navController,
            paddingValues = paddingValues
        )

        messagingGraph(
            navController = navController,
            paddingValues = paddingValues
        )

        onBoardingGraph(
            navController = navController,
            paddingValues = paddingValues
        )

        settingGraph(
            navController = navController,
            paddingValues = paddingValues,
            onToggleTheme = onToggleTheme
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

fun NavController.navigateScreen(destination: String) {
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}