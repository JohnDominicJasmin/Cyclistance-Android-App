package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.UserItemDto
import com.example.cyclistance.feature_mapping.domain.model.CardModel
import com.example.cyclistance.feature_mapping.domain.model.User
import com.example.cyclistance.feature_mapping.domain.model.UserItem

object UserMapper {

    fun UserItemDto.toUserItem(): UserItem {
        return UserItem(
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


    fun UserItem.toUserItemDto():UserItemDto{
        return UserItemDto(
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


    fun UserDto.toUser(): User{
        return User(
            users = this.map { it.toUserItem() }
        )
    }




    fun UserItem.toCardModel(distance: String = "---", eta: String = "---"): CardModel {
        return CardModel(
            id = this.id,
            name = this.name,
            profileImageUrl = this.profilePictureUrl,
            distance = distance,
            estimatedTimeTravel = eta,
            address = this.address
        )
    }





}