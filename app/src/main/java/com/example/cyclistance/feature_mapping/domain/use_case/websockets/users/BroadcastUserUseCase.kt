package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class BroadcastUserUseCase(private val repository: MappingRepository, private val context: Context) {
    operator fun invoke() {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastUser()
    }
}