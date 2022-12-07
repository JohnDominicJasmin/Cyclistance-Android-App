package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappingBannerModel(
    val userProfileImage: Int,
    val name: String,
    val issue: String,
    val bikeType: String,
    val distanceRemaining: String,
    val timeRemaining: String,
    val address: String,
    val message: String
):Parcelable

