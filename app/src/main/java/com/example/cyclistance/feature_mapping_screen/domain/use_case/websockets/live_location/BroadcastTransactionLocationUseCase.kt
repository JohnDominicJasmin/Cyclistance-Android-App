package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location

import android.content.Context
import com.example.cyclistance.feature_mapping_screen.data.network_observer.NetworkConnectivityUtil
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping_screen.domain.model.RescueTransactionItem
import com.example.cyclistance.feature_mapping_screen.domain.model.UserItem
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastTransactionLocationUseCase(
    private val repository: MappingRepository,
    private val context: Context) {
    operator fun invoke(
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

        if (NetworkConnectivityUtil(context).hasInternet().not()) {
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastLocation(locationModel)
    }
}