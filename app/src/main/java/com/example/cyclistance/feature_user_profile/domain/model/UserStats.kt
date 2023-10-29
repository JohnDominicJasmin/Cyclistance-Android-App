package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStats(
    val rescueeId: String,
    val rescuerId: String,
    val rescueOverallDistanceInMeters: Double = 0.0,
    val rescueAverageSpeed: Double = 0.0,
    val rescueDescription: String = "",
):Parcelable
