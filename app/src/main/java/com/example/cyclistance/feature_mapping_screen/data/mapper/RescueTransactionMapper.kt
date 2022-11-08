package com.example.cyclistance.feature_mapping_screen.data.mapper

import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.RescueTransactionDto
import com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction.RescueTransactionItemDto
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem

object RescueTransactionMapper {
    fun RescueTransactionItemDto.toRescueTransaction():RescueTransactionItem {
        return RescueTransactionItem(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status
        )
    }

    fun RescueTransactionItem.toRescueTransactionDto():RescueTransactionItemDto{
        return RescueTransactionItemDto(
            id = this.id,
            cancellation = this.cancellation,
            rescueeId = this.rescueeId,
            rescuerId = this.rescuerId,
            status = this.status
        )
    }

    fun RescueTransactionDto.toRescueTransaction():RescueTransaction{
        return RescueTransaction(
            rescueTransactions = this.map { it.toRescueTransaction() }
        )
    }



}