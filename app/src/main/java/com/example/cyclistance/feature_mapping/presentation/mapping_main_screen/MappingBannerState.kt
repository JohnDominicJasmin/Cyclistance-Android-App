package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen

import androidx.annotation.DrawableRes

data class MappingBannerState(
    @DrawableRes val userProfileImage:Int,
    val name:String,
    val issue:String,
    val distanceRemaining:String,
    val timeRemaining:String,
    val address:String,
    val message:String
)
