package com.example.cyclistance.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cyclistance.common.NoInternetScreen

import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_mapping.presentation.components.MappingScreen
import com.example.cyclistance.feature_readable_displays.presentation.intro_slider.components.IntroSliderScreen

import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen


@Composable
fun Navigation(navController:NavHostController, onBackPressed:() -> Unit) {




    NavHost(navController = navController, startDestination = Screens.SplashScreen.route){

        composable(Screens.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(Screens.IntroSliderScreen.route){
            IntroSliderScreen(navController = navController, onBackPressed = onBackPressed)
        }

        composable(Screens.SignInScreen.route){
            SignInScreen(navController = navController, onBackPressed = onBackPressed)
        }

        composable(Screens.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }

        composable(Screens.EmailAuthScreen.route){
            EmailAuthScreen(navController = navController, onBackPressed = onBackPressed)
        }

        composable(Screens.MappingScreen.route){
            MappingScreen(navController = navController, onBackPressed = onBackPressed)
        }
        composable(Screens.NoInternetScreen.route){
            NoInternetScreen(navController = navController, onBackPressed = onBackPressed)
        }

    }
}