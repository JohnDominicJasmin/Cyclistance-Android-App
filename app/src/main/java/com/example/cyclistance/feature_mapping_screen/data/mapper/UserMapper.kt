package com.example.cyclistance.feature_mapping_screen.data.mapper

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.*
import com.example.cyclistance.feature_mapping_screen.domain.model.*

object UserMapper {

fun UserDto.toUser():User{
    return User(
        address = this.address,
        contactNumber = this.contactNumber,
        id = this.id,
        location = this.location,
        name = this.name,
        profilePictureUrl = this.profilePictureUrl,
        userNeededHelp = this.userNeededHelp,
        userAssistance = this.userAssistance,
        rescueRequest = this.rescueRequest,

    )
}
}