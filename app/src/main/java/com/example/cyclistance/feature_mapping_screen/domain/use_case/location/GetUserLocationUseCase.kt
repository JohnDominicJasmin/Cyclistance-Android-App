package com.example.cyclistance.feature_mapping_screen.domain.use_case.location

import android.location.Location
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<Location> {
        return repository.getUserLocation()
    }
}