package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class RescueRequest(
    @SerializedName("respondents")
    val respondents: List<Respondent> = emptyList()
)