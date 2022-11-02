package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.RescueRequest
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Transaction
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserAssistance
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserItem(
    val address: String? = null,
    val contactNumber: String? = null,
    val id: String? = null,
    val location: Location? = null,
    val name: String? = null,
    val profilePictureUrl: String? = null,
    val rescueRequest: RescueRequest? = null,
    val transaction: Transaction? = null,
    val userAssistance: UserAssistance? = null,
): Parcelable