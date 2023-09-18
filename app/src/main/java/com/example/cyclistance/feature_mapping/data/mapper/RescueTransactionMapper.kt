package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.CancellationDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.CancellationReasonDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RescueTransactionDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RescueTransactionItemDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.RouteDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.rescue_transaction.StatusDto
import com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto.LocationDto
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.CancellationModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.CancellationReasonModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RouteModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.StatusModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel

object RescueTransactionMapper {
    fun RescueTransactionItemDto.toRescueTransaction(): RescueTransactionItem {
        return RescueTransactionItem(
            id = this.id,
            cancellation = this.cancellation?.toCancellationModel(),
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status?.toStatusModel(),
            route = this.route?.toRouteModel()
        )
    }

    private fun CancellationDto.toCancellationModel(): CancellationModel {
        return CancellationModel(
            cancellationReason = this.cancellationReason.toCancellationReasonModel()
        )
    }

    private fun CancellationReasonDto.toCancellationReasonModel(): CancellationReasonModel {
        return CancellationReasonModel(
            message = this.message,
            reason = this.reason
        )
    }

    private fun StatusDto.toStatusModel(): StatusModel{
        return StatusModel(
            finished = this.finished,
            onGoing = this.ongoing,
            started = this.started
        )
    }

    private fun RouteDto.toRouteModel():RouteModel{
        return RouteModel(
            startingLocation = this.startingLocation.toLocation(),
            destinationLocation = this.destinationLocation.toLocation(),
        )
    }

     fun LocationDto.toLocation(): LocationModel{
        return LocationModel(
            latitude = this.latitude,
            longitude = this.longitude
        )
    }

    fun LocationModel.toLocationDto(): LocationDto {
        return LocationDto(
            latitude = this.latitude,
            longitude = this.longitude
        )
    }

    fun RescueTransactionItem.toRescueTransactionDto(): RescueTransactionItemDto {
        return RescueTransactionItemDto(
            id = this.id,
            cancellation = this.cancellation?.toCancellationDto(),
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status?.toStatusDto(),
            route = this.route?.toRouteDto()
        )
    }

    private fun RouteModel.toRouteDto(): RouteDto {
        return RouteDto(
            startingLocation = this.startingLocation.toLocationDto(),
            destinationLocation = this.destinationLocation.toLocationDto()
        )
    }

    private fun CancellationModel.toCancellationDto(): CancellationDto {
        return CancellationDto(
            cancellationReason = this.cancellationReason.toCancellationReasonDto(),
            idCancelledBy = this.idCancelledBy,
            nameCancelledBy = this.nameCancelledBy,
            rescueCancelled = this.rescueCancelled
        )
    }

    private fun CancellationReasonModel.toCancellationReasonDto(): CancellationReasonDto {
        return CancellationReasonDto(
            message = this.message,
            reason = this.reason
        )
    }

    private fun StatusModel.toStatusDto(): StatusDto {
        return StatusDto(
            finished = this.finished,
            ongoing = this.onGoing,
            started = this.started
        )
    }


    fun RescueTransactionDto.toRescueTransaction(): RescueTransaction {
        return RescueTransaction(
            transactions = this.map { it.toRescueTransaction() }
        )
    }


}