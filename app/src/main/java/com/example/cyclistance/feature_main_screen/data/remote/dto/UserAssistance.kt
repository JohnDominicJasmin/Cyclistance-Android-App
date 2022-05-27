package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class UserAssistance(
    @SerializedName("confirmationDetails")
    val confirmationDetails: ConfirmationDetails,
    @SerializedName("status")
    val status: Status
)