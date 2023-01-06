package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastTransactionLocationUseCase(
    private val repository: MappingRepository,
    private val context: Context) {
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

        if (context.hasInternetConnection().not()) {
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastLocation(locationModel)
    }
}