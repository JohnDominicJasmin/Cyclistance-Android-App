package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.ConfirmationDetail
import com.example.cyclistance.feature_main_screen.data.remote.dto.Status

data class UserAssistance(
    val id:String = "",
    val confirmationDetail: ConfirmationDetail,
    val status: Status? = null
)
