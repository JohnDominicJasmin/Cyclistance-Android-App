package com.example.cyclistance.feature_mapping.domain.use_case.location

import com.example.cyclistance.feature_mapping.domain.model.api.user.LocationModel
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<LocationModel> {
        return repository.getUserLocation()
    }
}