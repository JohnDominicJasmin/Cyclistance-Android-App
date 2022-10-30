package com.example.cyclistance.feature_mapping_screen.data.mapper

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.RescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction

object RescueTransactionMapper {
    fun RescueTransactionDto.toRescueTransaction():RescueTransaction {
        return RescueTransaction(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status
        )
    }

    fun RescueTransaction.toRescueTransactionDto():RescueTransactionDto{
        return RescueTransactionDto(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status
        )
    }

}