package com.example.cyclistance.feature_rescue_record.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideDetails(
    val rideId: String = "",
    val rescuerId: String = "",
    val rescuerName: String = "",
    val rescuerPhotoUrl: String = "",
    val rescueeId: String = "",
    val rescueeName: String = "",
    val rescueePhotoUrl: String = "",
    val rideSummary: RideSummary = RideSummary()
) : Parcelable
