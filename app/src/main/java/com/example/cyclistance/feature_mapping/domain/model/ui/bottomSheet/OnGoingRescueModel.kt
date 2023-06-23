package com.example.cyclistance.feature_mapping.domain.model.ui.bottomSheet

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class OnGoingRescueModel(
    val currentSpeed: String = "",
    val ridingDistance: String = "",
    val maxSpeed: String = "",
    val ridingTime: String = "",
    val estimatedTime: String = "",
    val estimatedDistance: String = "",

    ) : Parcelable
