package com.example.cyclistance.navigation

sealed class Screens(val route:String){

    object SplashScreen: Screens(route = "splash_screen")
    object SignInScreen: Screens(route = "sign_in_screen")
    object SignUpScreen: Screens(route = "sign_up_screen")
    object EmailAuthScreen: Screens(route = "email_auth_screen")
    object IntroSliderScreen: Screens(route="intro_slider_screen")
}
