package com.example.cyclistance.navigation.nav_graph

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.cyclistance.feature_on_boarding.presentation.IntroSliderScreen
import com.example.cyclistance.navigation.Screens

fun NavGraphBuilder.onBoardingGraph(
    navController: NavController,
    paddingValues: PaddingValues
) {
    navigation(
        startDestination = Screens.OnBoardingNavigation.IntroSlider.screenRoute,
        route = Screens.OnBoardingNavigation.ROUTE
    ) {
        composable(Screens.OnBoardingNavigation.IntroSlider.screenRoute) {
            IntroSliderScreen(
                navController = navController,
                paddingValues = paddingValues)
        }

    }
}
