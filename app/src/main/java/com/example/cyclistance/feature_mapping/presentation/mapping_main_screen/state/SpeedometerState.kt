package com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class SpeedometerState(
    val travelledDistance: String = "",
    val topSpeed: Double = 0.0,
    val currentSpeedKph: Double = 0.0,
):Parcelable

