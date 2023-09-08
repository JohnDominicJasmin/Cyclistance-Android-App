package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.example.cyclistance.core.utils.annotations.StableState
import com.google.gson.annotations.SerializedName


@StableState
data class LocationDto(
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
)