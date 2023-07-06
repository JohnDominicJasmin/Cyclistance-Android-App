package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class RespondentDto(
    @SerializedName("client_id")
    val clientId: String = ""
)