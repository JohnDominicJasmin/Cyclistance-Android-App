package com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.google.gson.annotations.SerializedName
import com.myapp.cyclistance.core.utils.annotations.StableState


@StableState
data class LocationDto(
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
)