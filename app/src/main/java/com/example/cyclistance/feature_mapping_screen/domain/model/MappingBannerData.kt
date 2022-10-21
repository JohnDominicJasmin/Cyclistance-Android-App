package com.example.cyclistance.feature_mapping_screen.domain.model


data class MappingBannerData(
    val userProfileImage: Int,
    val name: String,
    val issue: String,
    val bikeType: String,
    val distanceRemaining: String,
    val timeRemaining: String,
    val address: String,
    val message: String
)

