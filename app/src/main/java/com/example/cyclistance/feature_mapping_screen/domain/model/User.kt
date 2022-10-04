package com.example.cyclistance.feature_mapping_screen.domain.model

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.RescueRequest
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.UserAssistance

data class User(
    val address: String? = null,
    val contactNumber: String? = null,
    val id: String? = null,
    val location: Location? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val rescueRequest: RescueRequest = RescueRequest(),
    val userAssistance: UserAssistance = UserAssistance(),
    val userNeededHelp: Boolean = false
)