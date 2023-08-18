package com.example.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
    val speed: Double = 0.0
):Parcelable
