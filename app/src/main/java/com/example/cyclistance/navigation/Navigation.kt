package com.example.cyclistance.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cyclistance.common.NoInternetScreen

import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_main_screen.presentation.mapping_main_screen.components.MappingScreen
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderScreen

import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen


@Composable
fun Navigation(navController: NavHostController, onBackPressed: () -> Unit) {


    NavHost(navController = navController, startDestination = Screens.SplashScreen.route) {

        composable(Screens.SplashScreen.route) {
            SplashScreen { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }


        composable(Screens.IntroSliderScreen.route) {
            IntroSliderScreen(onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.SignInScreen.route) {
            SignInScreen(onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.SignUpScreen.route) {
            SignUpScreen { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.EmailAuthScreen.route) {
            EmailAuthScreen(onBackPressed = onBackPressed) { destination, popUpToDestination ->
                navigateScreen(navController, destination, popUpToDestination)
            }
        }

        composable(Screens.MappingScreen.route) {
            MappingScreen(onBackPressed = onBackPressed) {

            }
        }
        composable(Screens.NoInternetScreen.route) {
            NoInternetScreen(onBackPressed = onBackPressed) {
                navController.popBackStack()
            }
        }

    }
}

private fun navigateScreen(
    navController: NavHostController,
    destination: String,
    popUpToDestination: String? = null) {


    navController.navigate(destination) {
        popUpToDestination?.let {
            popUpTo(it) {
                inclusive = true
            }
        }
        launchSingleTop = true
    }
}