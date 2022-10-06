package com.example.cyclistance.feature_main_screen.domain.use_case.location

import com.example.cyclistance.core.utils.SharedLocationModel
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetUserLocationUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<SharedLocationModel> {
        return repository.getUserLocation()
    }
}