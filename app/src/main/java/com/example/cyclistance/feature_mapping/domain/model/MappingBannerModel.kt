package com.example.cyclistance.feature_mapping.domain.model

import androidx.annotation.DrawableRes

data class MappingBannerModel(
    @DrawableRes val userProfileImage:Int,
    val name:String,
    val issue:String,
    val distanceRemaining:String,
    val timeRemaining:String,
    val address:String,
    val message:String
)
