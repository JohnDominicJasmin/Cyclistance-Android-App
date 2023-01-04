package com.example.cyclistance.feature_readable_displays.presentation.intro_slider

import android.os.Parcelable
import com.example.cyclistance.navigation.Screens
import kotlinx.parcelize.Parcelize


@Parcelize
data class MainScreenState(
    val navigationStartingDestination:String = Screens.IntroSliderScreen.route
):Parcelable
