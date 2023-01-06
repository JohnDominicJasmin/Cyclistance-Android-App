package com.example.cyclistance.feature_mapping.domain.use_case.websockets.users

import android.content.Context
import com.example.cyclistance.feature_mapping.data.location.ConnectionStatus.hasInternetConnection
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.example.cyclistance.feature_mapping.domain.model.User
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserUpdatesUseCase(private val context: Context,private val repository: MappingRepository) {
    operator fun invoke(): Flow<User>{

        if(context.hasInternetConnection().not()){
            throw MappingExceptions.NetworkException()
        }

        return repository.getUserUpdates()
    }
}