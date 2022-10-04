package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CancellationReason(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("reason")
    val reason: String = ""
)