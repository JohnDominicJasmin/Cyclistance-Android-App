package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class StatusDto(
    @SerializedName("finished")
    val finished: Boolean = false,
    @SerializedName("ongoing")
    val ongoing: Boolean = false,
    @SerializedName("started")
    val started: Boolean = false
)