package com.myapp.cyclistance.feature_mapping.domain.use_case.location

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.LocationModel
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(): Flow<LocationModel> {
        return repository.getUserLocation()
    }
}