package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class UserAssistanceDto(
    @SerializedName("confirmationDetails")
    val confirmationDetails: ConfirmationDetails,
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: Status
)