package com.example.cyclistance

sealed class Screen(val route:String){

    object SplashScreen:Screen(route = "splash_screen")
    object MainScreen:Screen(route ="main_screen")

}
