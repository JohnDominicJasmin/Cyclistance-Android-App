package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CancellationReasonDto(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("reason")
    val reason: String = ""
)