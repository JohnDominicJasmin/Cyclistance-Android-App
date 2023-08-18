package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import com.example.cyclistance.feature_mapping.domain.model.remote_models.live_location.LiveLocationSocketModel
import com.example.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingSocketRepository
import kotlinx.coroutines.flow.Flow

class TransactionLocationUseCase(
    private val repository: MappingSocketRepository) {
    suspend operator fun invoke(
        locationModel: LiveLocationSocketModel,
        user: UserItem,
        rescueTransactionItem: RescueTransactionItem) {

        if(user.transaction?.transactionId.isNullOrEmpty()){
            return
        }

        if (rescueTransactionItem.id.isNullOrEmpty()) {
            return
        }

        if(rescueTransactionItem.isRescueCancelled()){
            return
        }

        repository.broadcastTransactionLocation(locationModel)
    }
    suspend operator fun invoke(): Flow<LiveLocationSocketModel> {
        return repository.getTransactionLocationUpdates()
    }
}