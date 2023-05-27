package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ConfirmationDetailDto(
    @SerializedName("bike_type")
    val bikeType: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("message")
    val message: String = ""
)