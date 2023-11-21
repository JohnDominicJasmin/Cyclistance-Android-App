package com.myapp.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserActivityModel(
    val requestAssistanceFrequency: Int,
    val rescueFrequency: Int,
    val overallDistanceOfRescueInMeters: Double,
    val averageSpeed: Double
):Parcelable{
    @StableState
    constructor(): this(
        requestAssistanceFrequency = 0,
        rescueFrequency = 0,
        overallDistanceOfRescueInMeters = 0.0,
        averageSpeed = 0.0
    )
}


