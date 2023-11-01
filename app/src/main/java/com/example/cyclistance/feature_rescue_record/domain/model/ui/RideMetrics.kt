package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideMetrics(
    val distanceInMeters: Double = 0.0,
    val maxSpeed: String = "0.0km/h",
    val averageSpeedMps: Double = 0.0
):Parcelable{

}
