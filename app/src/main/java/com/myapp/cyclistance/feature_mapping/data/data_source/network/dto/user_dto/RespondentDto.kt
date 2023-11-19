package com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.google.gson.annotations.SerializedName

data class RespondentDto(
    @SerializedName("client_id")
    val clientId: String = "",
    @SerializedName("timestamp")
    val timeStamp: Long = 0L,
)