package com.myapp.cyclistance.feature_mapping.domain.model.ui.bottomSheet

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class OnGoingRescueModel(
    val currentSpeed: String = "",
    val ridingDistance: String = "",
    val maxSpeed: String = "",
    val estimatedTime: String = "",
    val estimatedDistance: String? = "",

    ) : Parcelable
