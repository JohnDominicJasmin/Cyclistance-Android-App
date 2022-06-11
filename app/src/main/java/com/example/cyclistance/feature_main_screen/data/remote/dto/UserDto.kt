package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("address")
    val address: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: String,
    @SerializedName("user_needed_help")
    val userNeededHelp: Boolean = false,
    @SerializedName("user_assistance")
    val userAssistance: UserAssistance = UserAssistance(),
    @SerializedName("profile_picture_url")
    val profilePictureUrl: String,
    @SerializedName("contact_number")
    val contactNumber: String
)