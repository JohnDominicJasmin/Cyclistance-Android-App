package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.ConfirmationDetailDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.RescueRequestDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.RespondentDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.TransactionDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserAssistanceDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserItemDto
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toLocationDto
import com.example.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toLocationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.ConfirmationDetailModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.RescueRequest
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.RespondentModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.TransactionModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserAssistanceModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem

object UserMapper {

    fun UserItemDto.toUserItem(): UserItem {
        return UserItem(
            address = this.address,
            contactNumber = this.contactNumber,
            id = this.id,
            location = this.location?.toLocationModel(),
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance?.toUserAssistanceModel(),
            rescueRequest = this.rescueRequest?.toRescueRequestModel(),
            transaction = this.transaction?.toTransactionModel()
            )
    }


    private fun RescueRequestDto.toRescueRequestModel(): RescueRequest {
        return RescueRequest(
            respondents = this.respondents.map { it.toRespondentModel() },
        )
    }

    fun UserItem.toUserItemDto(): UserItemDto {
        return UserItemDto(
            address = this.address,
            contactNumber = this.contactNumber,
            id = this.id,
            location = this.location?.toLocationDto(),
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance?.toUserAssistanceDto(),
            rescueRequest = this.rescueRequest?.toRescueRequestDto(),
            transaction = this.transaction?.toTransactionDto()
        )
    }


    private fun TransactionDto.toTransactionModel():TransactionModel{
        return TransactionModel(
            role = this.role,
            transactionId = this.transactionId
        )
    }

    private fun TransactionModel.toTransactionDto(): TransactionDto {
        return TransactionDto(
            role = this.role,
            transactionId = this.transactionId
        )
    }

    private fun RescueRequest.toRescueRequestDto(): RescueRequestDto {
        return RescueRequestDto(
            respondents = this.respondents.map { it.toRespondentDto() },
        )
    }

    private fun RespondentDto.toRespondentModel():RespondentModel{
        return RespondentModel(
            clientId = this.clientId
        )
    }

    private fun RespondentModel.toRespondentDto(): RespondentDto {
        return RespondentDto(
            clientId = this.clientId
        )
    }

    private fun ConfirmationDetailDto.toConfirmationModel(): ConfirmationDetailModel {
        return ConfirmationDetailModel(
            bikeType = this.bikeType,
            description = this.description,
            message = this.message
        )
    }

    private fun ConfirmationDetailModel.toConfirmationDetailDto(): ConfirmationDetailDto {
        return ConfirmationDetailDto(
            bikeType = this.bikeType,
            description = this.description,
            message = this.message
        )
    }

    private fun UserAssistanceDto.toUserAssistanceModel(): UserAssistanceModel{
        return UserAssistanceModel(
            confirmationDetail = this.confirmationDetail.toConfirmationModel(),
            needHelp = this.needHelp,
        )
    }

    private fun UserAssistanceModel.toUserAssistanceDto(): UserAssistanceDto {
        return UserAssistanceDto(
            confirmationDetail = this.confirmationDetail.toConfirmationDetailDto(),
            needHelp = this.needHelp,
        )
    }




    fun UserDto.toUser(): NearbyCyclist {
        return NearbyCyclist(
            users = this.map { it.toUserItem() }
        )
    }

    fun UserItem.toRescueRequest(distance: String = "---", eta: String = "---"): RescueRequestItemModel {
        return RescueRequestItemModel(
            id = this.id,
            name = this.name,
            profileImageUrl = this.profilePictureUrl,
            distance = distance,
            estimatedTimeTravel = eta,
            address = this.address
        )
    }





}