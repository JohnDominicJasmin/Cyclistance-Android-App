package com.example.cyclistance.feature_mapping.data.mapper

import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.RescueTransactionDto
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.RescueTransactionItemDto
import com.example.cyclistance.feature_mapping.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem

object RescueTransactionMapper {
    fun RescueTransactionItemDto.toRescueTransaction():RescueTransactionItem {
        return RescueTransactionItem(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status,
            route = this.route
        )
    }

    fun RescueTransactionItem.toRescueTransactionDto():RescueTransactionItemDto{
        return RescueTransactionItemDto(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status,
            route = this.route
        )
    }

    fun RescueTransactionDto.toRescueTransaction():RescueTransaction{
        return RescueTransaction(
            rescueTransactions = this.map { it.toRescueTransaction() }
        )
    }



}