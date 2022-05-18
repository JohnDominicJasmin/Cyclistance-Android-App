package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class RescueRequest(
    @SerializedName("respondents")
    val respondents: List<Respondent>
)