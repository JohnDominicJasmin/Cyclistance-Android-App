package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideMetrics(
    val distanceInMeters: Double,
    val maxSpeed: String,
    val averageSpeedMps: Double
):Parcelable{
    @StableState
    constructor(): this(
        distanceInMeters = 0.0,
        maxSpeed = "0.0km/h",
        averageSpeedMps = 0.0)
}
