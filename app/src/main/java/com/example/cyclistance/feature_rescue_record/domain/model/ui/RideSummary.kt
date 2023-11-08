package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideSummary(
    val rating: Double,
    val ratingText: String,
    val iconDescription: String,
    val bikeType: String,
    val date: String,
    val startingTime: String,
    val endTime: String,
    val startingAddress: String,
    val destinationAddress: String,
    val duration: String,

    ) : Parcelable{

        @StableState
        constructor(): this(
            rating = 0.0,
            ratingText = "",
            iconDescription = "",
            bikeType = "",
            date = "",
            startingTime = "",
            endTime = "",
            startingAddress = "",
            destinationAddress = "",
            duration = "",
        )
    }
