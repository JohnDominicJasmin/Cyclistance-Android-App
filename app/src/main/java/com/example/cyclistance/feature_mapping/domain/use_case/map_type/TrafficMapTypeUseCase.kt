package com.example.cyclistance.feature_mapping.domain.use_case.map_type

import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.flow.Flow

class TrafficMapTypeUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke(isSelected: Boolean) {
        repository.setTrafficMapTypeSelected(isSelected)
    }
    suspend operator fun invoke(): Flow<Boolean> {
        return repository.isTrafficMapTypeSelected()
    }
}