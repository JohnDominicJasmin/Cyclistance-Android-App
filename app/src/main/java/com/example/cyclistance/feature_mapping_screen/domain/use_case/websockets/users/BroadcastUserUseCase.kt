package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.users

import android.content.Context
import com.example.cyclistance.feature_mapping_screen.data.network_observer.NetworkConnectivityUtil
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastUserUseCase(private val repository: MappingRepository, private val context: Context) {
    operator fun invoke() {
        if(NetworkConnectivityUtil(context).hasInternet().not()){
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastUser()
    }
}