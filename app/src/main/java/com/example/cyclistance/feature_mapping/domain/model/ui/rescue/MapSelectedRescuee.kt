package com.example.cyclistance.feature_mapping.domain.model.ui.rescue

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize
@Parcelize
@Immutable
@Stable
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

