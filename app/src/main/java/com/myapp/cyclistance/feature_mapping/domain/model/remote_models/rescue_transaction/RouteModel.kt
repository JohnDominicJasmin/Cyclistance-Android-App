package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction

import android.os.Parcelable
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class RouteModel(
    val startingLocation: LocationModel = LocationModel(),
    val destinationLocation: LocationModel = LocationModel(),
):Parcelable
