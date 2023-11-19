package com.myapp.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
import java.util.Date

@StableState
@Parcelize
data class RescueRide(
    val rideId: String,
    val rescuerId: String,
    val rescuerName: String,
    val rescuerPhotoUrl: String,
    val rescueeId: String,
    val rescueeName: String,
    val rescueePhotoUrl: String,
    val rideDate: Date,
    val rideSummary: RideSummary,
    val rideMetrics: RideMetrics) : Parcelable{
        @StableState
        constructor(): this(
            rideId = "",
            rescuerId = "",
            rescuerName = "",
            rescuerPhotoUrl = "",
            rescueeId = "",
            rescueeName = "",
            rescueePhotoUrl = "",
            rideDate = Date(),
            rideSummary = RideSummary(),
            rideMetrics = RideMetrics()
        )
    }
