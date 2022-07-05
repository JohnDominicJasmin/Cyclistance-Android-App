package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class ConfirmationDetails(
    @SerializedName("bike_type")
    val bikeType: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("message")
    val message: String = ""
)