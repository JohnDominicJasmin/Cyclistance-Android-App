package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationModel(
    val latitude: Double? = null,
    val longitude: Double? = null,
):Parcelable
