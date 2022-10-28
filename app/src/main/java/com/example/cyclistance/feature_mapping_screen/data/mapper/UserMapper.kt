package com.example.cyclistance.feature_mapping_screen.data.mapper

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping_screen.domain.model.*

object UserMapper {

    fun UserDto.toUser(): User {
        return User(
            address = this.address,
            contactNumber = this.contactNumber,
            id = this.id,
            location = this.location,
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance,
            rescueRequest = this.rescueRequest,
            transaction = this.transaction
            )
    }


    fun User.toUserDto():UserDto{
        return UserDto(
            address = this.address,
            contactNumber = this.contactNumber,
            id = this.id,
            location = this.location,
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance,
            rescueRequest = this.rescueRequest,
            transaction = this.transaction
        )
    }


    fun User.toCardModel(): CardModel {
        return CardModel(
            id = this.id,
            name = this.name,
            profileImageUrl = this.profilePictureUrl,
            distance = "0.5km", //todo: compute later
            estimatedTimeTravel = "5 mins",
            address = this.address
        )
    }
}