package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class UserDto(
    @SerializedName("address")
    val address: String?,
    @SerializedName("contact_number")
    val contactNumber: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("location")
    val location: Location?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("profile_picture_url")
    val profilePictureUrl: String?,
    @SerializedName("rescue_request")
    val rescueRequest: RescueRequest = RescueRequest(),
    @SerializedName("user_assistance")
    val userAssistance: UserAssistance = UserAssistance(),
    @SerializedName("user_needed_help")
    val userNeededHelp: Boolean = false
)