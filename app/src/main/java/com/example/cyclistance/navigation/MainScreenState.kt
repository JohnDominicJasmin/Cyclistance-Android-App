package com.example.cyclistance.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class MainScreenState(
    val navigationStartingDestination:String = Screens.IntroSliderScreen.route
):Parcelable
