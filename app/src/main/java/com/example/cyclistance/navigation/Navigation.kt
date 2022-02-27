package com.example.cyclistance.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_authentication.presentation.authentication_email.components.EmailAuthScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_readable_displays.presentation.IntroSliderScreen
import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.SplashScreen.route){

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