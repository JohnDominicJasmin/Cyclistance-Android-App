package com.myapp.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class RideHistoryItem(
    val id: String,
    val photoUrl: String,
    val date: String,
    val duration: String,
    val role: String,
    val rescueDescription: String,
    val startingAddress: String,
    val destinationAddress: String,
) : Parcelable{
    @StableState
    constructor(): this(
        id = "",
        photoUrl = "",
        date = "",
        duration = "",
        role = "",
        rescueDescription = "",
        startingAddress = "",
        destinationAddress = "",
    )
}
