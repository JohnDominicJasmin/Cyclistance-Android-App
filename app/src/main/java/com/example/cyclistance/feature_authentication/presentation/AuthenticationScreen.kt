package com.example.cyclistance.feature_authentication.presentation

sealed class AuthenticationScreen(val route:String){

    object SplashScreen: AuthenticationScreen(route = "splash_screen")
    object SignInScreen: AuthenticationScreen(route = "sign_in_screen")
    object SignUpScreen: AuthenticationScreen(route = "sign_up_screen")
}
