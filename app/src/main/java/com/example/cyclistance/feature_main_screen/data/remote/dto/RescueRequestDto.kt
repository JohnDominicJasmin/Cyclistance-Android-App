package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RescueRequestDto(
    @SerializedName("rescue_event_id")
    val rescueEventId: String,
    @SerializedName("respondents")
    val respondents: List<Respondent>
)