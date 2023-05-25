package com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction

import android.os.Parcelable
import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteModel(
    val startingLocation: LocationModel = LocationModel(),
    val destinationLocation: LocationModel = LocationModel(),
):Parcelable
