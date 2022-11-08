package com.example.cyclistance.feature_mapping_screen.domain.use_case.websockets

import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserUpdatesUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<User> = repository.getUserUpdates()
}