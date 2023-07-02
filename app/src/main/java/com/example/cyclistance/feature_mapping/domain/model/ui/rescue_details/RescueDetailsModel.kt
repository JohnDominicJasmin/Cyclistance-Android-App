package com.example.cyclistance.feature_mapping.domain.model.ui.rescue_details

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RescueDetailsModel(
    val rating: Double = 0.0,
    val ratingText: String = "",
    val textDescription: String = "",
    val bikeType: String = "",
    val date: String = "",
    val startingTime: String = "",
    val endTime: String = "",
    val startingAddress: String = "",
    val destinationAddress: String = "",
    val duration: String = "",
    val distance: String = "",
    val maxSpeed: String = "",


    ) : Parcelable
