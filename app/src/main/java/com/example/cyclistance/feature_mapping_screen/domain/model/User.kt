package com.example.cyclistance.feature_mapping_screen.domain.model

import android.os.Parcelable
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.RescueRequest
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.Transaction
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserAssistance
import kotlinx.parcelize.Parcelize
@Parcelize
data class User(
    val address: String? = "",
    val contactNumber: String? = "",
    val id: String? = "",
    val location: Location? = Location(),
    val name: String? = "",
    val profilePictureUrl: String? = "",
    val rescueRequest: RescueRequest? = RescueRequest(),
    val transaction: Transaction? = Transaction(),
    val userAssistance: UserAssistance? = UserAssistance(),
):Parcelable