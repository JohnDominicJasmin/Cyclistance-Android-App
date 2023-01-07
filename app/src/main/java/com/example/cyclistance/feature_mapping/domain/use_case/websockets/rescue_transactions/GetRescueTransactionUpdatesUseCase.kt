package com.example.cyclistance.feature_mapping.domain.use_case.websockets.rescue_transactions

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.RescueTransaction
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetRescueTransactionUpdatesUseCase(private val context: Context, private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<RescueTransaction> {


        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }
        return repository.getRescueTransactionUpdates()
    }


}