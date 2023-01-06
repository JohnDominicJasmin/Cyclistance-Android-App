package com.example.cyclistance.feature_mapping.domain.use_case.location

import android.location.Location
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<Location> {
        return repository.getUserLocation()
    }
}