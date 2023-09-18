package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.google.gson.annotations.SerializedName

data class ConfirmationDetailDto(
    @SerializedName("bike_type")
    val bikeType: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("message")
    val message: String = ""
)