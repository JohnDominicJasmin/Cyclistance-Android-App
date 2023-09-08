package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.google.gson.annotations.SerializedName


data class RescueRequestDto(
    @SerializedName("respondents")
    val respondents: List<RespondentDto> = emptyList()
)