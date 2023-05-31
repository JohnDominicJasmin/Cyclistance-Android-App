package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.api.user.UserItem
import com.example.cyclistance.feature_mapping.domain.model.location.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastTransactionLocationUseCase(
    private val repository: MappingRepository) {
    suspend operator fun invoke(
        locationModel: LiveLocationWSModel,
        user: UserItem,
        rescueTransactionItem: RescueTransactionItem) {

        if(user.transaction?.transactionId.isNullOrEmpty()){
            return
        }

        if (rescueTransactionItem.id.isNullOrEmpty()) {
            return
        }

        if(rescueTransactionItem.cancellation?.rescueCancelled == true){
            return
        }

        repository.broadcastTransactionLocation(locationModel)
    }
}