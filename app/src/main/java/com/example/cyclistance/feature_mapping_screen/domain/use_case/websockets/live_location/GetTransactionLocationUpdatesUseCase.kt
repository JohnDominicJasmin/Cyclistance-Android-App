package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets.live_location

import android.content.Context
import com.example.cyclistance.feature_mapping_screen.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping_screen.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping_screen.domain.model.LiveLocationWSModel
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionLocationUpdatesUseCase(private val context: Context, val repository: MappingRepository) {
    operator fun invoke(): Flow<LiveLocationWSModel>{

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        return repository.getTransactionLocationUpdates()
    }
}