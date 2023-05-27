package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import androidx.annotation.Keep
import com.example.cyclistance.core.utils.annotations.StableState
import com.google.gson.annotations.SerializedName

@Keep

@StableState
data class LocationDto(
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
)