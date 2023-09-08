package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction

import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.LocationDto
import com.google.gson.annotations.SerializedName


data class RouteDto(
    @SerializedName("starting_location")
    val startingLocation: LocationDto = LocationDto(),
    @SerializedName("destination_location")
    val destinationLocation: LocationDto = LocationDto()
    )
