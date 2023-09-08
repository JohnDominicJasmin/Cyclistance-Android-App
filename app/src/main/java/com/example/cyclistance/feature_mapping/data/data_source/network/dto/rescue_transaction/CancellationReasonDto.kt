package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction


import com.google.gson.annotations.SerializedName

data class CancellationReasonDto(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("reason")
    val reason: String = ""
)