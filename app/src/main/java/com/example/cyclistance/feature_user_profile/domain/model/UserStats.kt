package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserStats(
    val rescueeId: String,
    val rescuerId: String,
    val rescueOverallDistanceInMeters: Double,
    val rescueAverageSpeedMps: Double,
    val rescueDescription: String,
):Parcelable{
    @StableState
    constructor(): this(
        rescueeId = "",
        rescuerId = "",
        rescueOverallDistanceInMeters = 0.0,
        rescueAverageSpeedMps = 0.0,
        rescueDescription = "")
}
