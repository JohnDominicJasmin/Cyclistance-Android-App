package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class CancellationEventDto(
    @SerializedName("cancellation_reason")
    val cancellationReasons: List<CancellationReason>,
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("id")
    val id: String
)