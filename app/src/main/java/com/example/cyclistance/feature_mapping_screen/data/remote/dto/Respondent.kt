package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Respondent(
    @SerializedName("cancellation")
    val cancellation: Cancellation = Cancellation(),
    @SerializedName("client_id")
    val clientId: String = "",
    @SerializedName("request_accepted")
    val requestAccepted: Boolean = false
)