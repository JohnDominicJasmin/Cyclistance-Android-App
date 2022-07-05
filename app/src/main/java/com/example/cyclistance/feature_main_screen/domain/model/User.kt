package com.example.cyclistance.feature_main_screen.domain.model

import com.example.cyclistance.feature_main_screen.data.remote.dto.Location
import com.example.cyclistance.feature_main_screen.data.remote.dto.UserAssistance
import com.google.gson.annotations.SerializedName

data class User(
    val address: String? = null,
    val id: String? = null,
    val location: Location? = null,
    val name: String? = null,
    val userNeededHelp: Boolean = false,
    val userAssistance: UserAssistance = UserAssistance(),
    val profilePictureUrl: String? = null,
    val contactNumber: String? = null
)
