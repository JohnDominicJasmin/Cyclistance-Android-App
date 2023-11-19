package com.myapp.cyclistance.feature_mapping.data.mapper

import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.ConfirmationDetailDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.RescuePendingDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.RescueRequestDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.RespondentDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.TransactionDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserAssistanceDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserDto
import com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.UserItemDto
import com.myapp.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toLocation
import com.myapp.cyclistance.feature_mapping.data.mapper.RescueTransactionMapper.toLocationDto
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue.RescueRequestItemModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.ConfirmationDetailModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.NearbyCyclist
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.RescuePending
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.RescueRequest
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.RespondentModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.TransactionModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserAssistanceModel
import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem

object UserMapper {

    fun UserItemDto.toUserItem(): UserItem {
        return UserItem(
            address = this.address,
            id = this.id,
            location = this.location?.toLocation(),
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance?.toUserAssistance(),
            rescueRequest = this.rescueRequest?.toRescueRequest(),
            transaction = this.transaction?.toTransaction(),
            rescuePending = this.rescuePendingDto?.toRescuePending()
            )
    }



    fun UserDto.toUser(): NearbyCyclist {
        return NearbyCyclist(
            users = this.map { it.toUserItem() }
        )
    }

    fun UserItem.toRescueRequest(distance: String = "---", eta: String = "---", timestamp: Long): RescueRequestItemModel {
        return RescueRequestItemModel(
            id = this.id,
            name = this.name,
            profileImageUrl = this.profilePictureUrl,
            distance = distance,
            estimatedTimeTravel = eta,
            address = this.address,
            timestamp = timestamp
        )
    }


    fun UserItem.toUserItemDto(): UserItemDto {
        return UserItemDto(
            address = this.address,
            id = this.id,
            location = this.location?.toLocationDto(),
            name = this.name,
            profilePictureUrl = this.profilePictureUrl,
            userAssistance = this.userAssistance?.toUserAssistanceDto(),
            rescueRequest = this.rescueRequest?.toRescueRequestDto(),
            transaction = this.transaction?.toTransactionDto(),
            rescuePendingDto = this.rescuePending?.toRescuePendingDto()

        )
    }


    private fun RescuePending.toRescuePendingDto(): RescuePendingDto {
        return RescuePendingDto(
            respondents = this.respondents.map { it.toRespondentDto() }
        )
    }

    private fun RescuePendingDto.toRescuePending(): RescuePending {
        return RescuePending(
            respondents = this.respondents.map { it.toRespondentModel() }
        )
    }

    private fun RescueRequestDto.toRescueRequest(): RescueRequest {
        return RescueRequest(
            respondents = this.respondents.map { it.toRespondentModel() },
        )
    }

    private fun TransactionDto.toTransaction():TransactionModel{
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
            clientId = this.clientId,
            timeStamp = this.timeStamp
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

    private fun UserAssistanceDto.toUserAssistance(): UserAssistanceModel{
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











}