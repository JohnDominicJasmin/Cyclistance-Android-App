package com.example.cyclistance.feature_mapping.domain.use_case.websockets.live_location

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionLocationUpdatesUseCase(private val context: Context, val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<LiveLocationWSModel>{

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        return repository.getTransactionLocationUpdates()
    }
}