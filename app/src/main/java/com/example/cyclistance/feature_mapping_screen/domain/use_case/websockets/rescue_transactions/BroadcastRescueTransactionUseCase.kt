package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.rescue_transactions

import android.content.Context
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository

class BroadcastRescueTransactionUseCase(private val repository: MappingRepository, private val context: Context,) {
    operator fun invoke() {
        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        repository.broadcastRescueTransaction()
    }
}