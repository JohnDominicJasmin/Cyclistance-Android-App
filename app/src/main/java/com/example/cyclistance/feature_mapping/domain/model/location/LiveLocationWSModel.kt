package com.example.cyclistance.feature_mapping.domain.model.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LiveLocationWSModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val room: String? = null,
):Parcelable
