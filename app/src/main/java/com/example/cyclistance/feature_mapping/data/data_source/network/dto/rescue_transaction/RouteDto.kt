package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction

import androidx.annotation.Keep
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.LocationDto
import com.google.gson.annotations.SerializedName

@Keep

data class RouteDto(
    @SerializedName("starting_location")
    val startingLocation: LocationDto = LocationDto(),
    @SerializedName("destination_location")
    val destinationLocation: LocationDto = LocationDto()
    )
