package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep

data class RescueRequestDto(
    @SerializedName("respondents")
    val respondents: List<RespondentDto> = emptyList()
)