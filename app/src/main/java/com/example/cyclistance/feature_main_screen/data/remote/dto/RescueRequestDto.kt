package com.example.cyclistance.feature_main_screen.data.remote.dto

data class RescueRequestDto(
    val id:String = "",
    val respondents:List<Respondent> = emptyList()
)
