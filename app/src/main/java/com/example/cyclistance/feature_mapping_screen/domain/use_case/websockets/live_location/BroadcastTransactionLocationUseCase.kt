package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location

import android.content.Context
import com.example.cyclistance.core.utils.network_observer.NetworkConnectivityUtil
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastTransactionLocationUseCase(private val repository: MappingRepository, private val context: Context) {
    operator fun invoke(locationModel: LiveLocationWSModel) {
        if(NetworkConnectivityUtil(context).hasInternet().not()){
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastLocation(locationModel)
    }
}