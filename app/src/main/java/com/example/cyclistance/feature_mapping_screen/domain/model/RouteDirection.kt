package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteDirection(
    val geometry: String = "",
    val duration: Double = 0.00
):Parcelable
