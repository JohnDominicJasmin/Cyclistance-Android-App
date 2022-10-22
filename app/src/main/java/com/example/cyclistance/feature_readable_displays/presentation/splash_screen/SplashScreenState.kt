package com.example.cyclistance.feature_readable_displays.presentation.splash_screen

import android.os.Parcelable
import com.example.cyclistance.navigation.Screens
import kotlinx.parcelize.Parcelize


@Parcelize
data class SplashScreenState(
    val navigationStartingDestination:String = Screens.SplashScreen.route
):Parcelable
