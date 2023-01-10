package com.example.cyclistance.feature_mapping.domain.use_case.location

import com.example.cyclistance.feature_mapping.data.remote.dto.user_dto.Location
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<Location> {
        return repository.getUserLocation()
    }
}