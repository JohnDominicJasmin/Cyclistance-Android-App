package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.Respondent

data class RescueRequest(
    val id: String = "",
    val respondents: List<Respondent> = emptyList()
)
