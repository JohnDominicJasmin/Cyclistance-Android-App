package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserAssistance(
    @SerializedName("confirmationDetails")
    val confirmationDetails: ConfirmationDetails = ConfirmationDetails(),
    @SerializedName("status")
    val status: Status = Status()
)