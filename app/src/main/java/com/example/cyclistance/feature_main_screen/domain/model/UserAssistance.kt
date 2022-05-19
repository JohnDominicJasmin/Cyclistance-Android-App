package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.ConfirmationDetails
import com.example.cyclistance.feature_main_screen.data.remote.dto.RescueRequest
import com.example.cyclistance.feature_main_screen.data.remote.dto.Status
import com.google.gson.annotations.SerializedName

data class UserAssistance(
    val confirmationDetails: ConfirmationDetails,
    val id: String,
    val rescueRequest: RescueRequest,
    val status: Status
)
