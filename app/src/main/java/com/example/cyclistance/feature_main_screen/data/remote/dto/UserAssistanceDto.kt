package com.example.cyclistance.feature_main_screen.data.remote.dto

data class UserAssistanceDto(
    val id:String = "",
    val confirmationDetail: ConfirmationDetail,
    val status: Status? = null
)
