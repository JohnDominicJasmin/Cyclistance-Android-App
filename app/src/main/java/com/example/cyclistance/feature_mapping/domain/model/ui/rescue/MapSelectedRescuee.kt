package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize
@Parcelize
@StableState
data class MapSelectedRescuee(
    val userId: String = "",
    val userProfileImage: String = "",
    val name: String = "",
    val issue: String = "",
    val bikeType: String = "",
    val distanceRemaining: String = "",
    val timeRemaining: String = "",
    val address: String = "",
    val message: String = ""
):Parcelable

