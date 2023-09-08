package com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction


import com.google.gson.annotations.SerializedName

data class StatusDto(
    @SerializedName("finished")
    val finished: Boolean = false,
    @SerializedName("ongoing")
    val ongoing: Boolean = false,
    @SerializedName("started")
    val started: Boolean = false
)