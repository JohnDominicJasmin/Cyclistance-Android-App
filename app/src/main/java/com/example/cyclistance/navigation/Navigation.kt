package com.example.cyclistance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_readable_displays.presentation.IntroSliderScreen
import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.SplashScreenViewModel

import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen


@Composable
fun Navigation(navController:NavHostController) {
    val splashScreenViewModel: SplashScreenViewModel = hiltViewModel()
    val screen by splashScreenViewModel._state

    NavHost(navController = navController, startDestination = screen.navigationStartingDestination){

        composable(Screens.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(Screens.IntroSliderScreen.route){
            IntroSliderScreen(navController = navController)
        }

        composable(Screens.SignInScreen.route){
            SignInScreen(navController)
        }

        composable(Screens.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }

        composable(Screens.EmailAuthScreen.route){
            EmailAuthScreen(navController = navController)
        }

    }
}