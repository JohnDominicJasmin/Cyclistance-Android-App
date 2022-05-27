package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.Location
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserAssistance
import com.google.gson.annotations.SerializedName

data class User(
    val address: String,
    val id: String,
    val location: Location,
    val name: String,
    val userNeededHelp: Boolean = false,
    val userAssistance: UserAssistance = UserAssistance()
)
