package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserActivityModel(
    val requestAssistanceFrequency: Int = 0,
    val rescueFrequency: Int = 0,
    val overallDistanceOfRescueInMeters: Double = 0.0,
    val averageSpeed: Double = 0.0
):Parcelable


