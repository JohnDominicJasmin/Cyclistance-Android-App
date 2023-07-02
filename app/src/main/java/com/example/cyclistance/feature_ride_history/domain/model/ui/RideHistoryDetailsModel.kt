package com.example.cyclistance.feature_ride_history.domain.model.ui

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_mapping.domain.model.ui.rescue_details.RescueDetailsModel
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class RideHistoryDetailsModel(
    val rescuerId: String = "",
    val rescuerName: String = "",
    val rescuerPhotoUrl: String = "",
    val rescueeId: String = "",
    val rescueeName: String = "",
    val rescueePhotoUrl: String = "",
    val rescueDetailsModel: RescueDetailsModel = RescueDetailsModel()
) : Parcelable
