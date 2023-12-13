package com.myapp.cyclistance.feature_rescue_record.data.local.entities

data class RideSummaryInfo(
    val rating: Double = 0.0,
    val ratingText: String = "",
    val iconDescription: String = "",
    val bikeType: String = "",
    val date: String = "",
    val startingTime: String = "",
    val endTime: String = "",
    val startingAddress: String = "",
    val destinationAddress: String = "",
    val duration: String = "",
)
