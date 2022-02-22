package com.example.cyclistance

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cyclistance.feature_authentication.presentation.AuthenticationScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.SignInScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.SignUpScreen
import com.example.cyclistance.feature_readable_displays.presentation.splash_screen.components.SplashScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AuthenticationScreen.SplashScreen.route){

        composable(AuthenticationScreen.SplashScreen.route){
            SplashScreen(navController = navController)

        }

        composable(AuthenticationScreen.SignInScreen.route){
            SignInScreen(navController)
        }

        composable(AuthenticationScreen.SignUpScreen.route){
            SignUpScreen(navController = navController)
        }


    }
}