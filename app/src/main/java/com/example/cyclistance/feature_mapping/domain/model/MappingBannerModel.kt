package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappingBannerModel(
    val userId: String = "",
    val userProfileImage: String,
    val name: String,
    val issue: String,
    val bikeType: String,
    val distanceRemaining: String,
    val timeRemaining: String,
    val address: String,
    val message: String
):Parcelable

