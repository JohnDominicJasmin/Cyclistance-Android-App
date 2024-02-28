package com.myapp.cyclistance.feature_mapping.domain.use_case.map_type

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.flow.Flow

class HazardousMapTypeUseCase(private val repository: MappingUiStoreRepository) {

    suspend operator fun invoke(isSelected: Boolean) {
        repository.setHazardousMapTypeSelected(isSelected)
    }
    suspend operator fun invoke(): Flow<Boolean> {
        return repository.isHazardousMapTypeSelected()
    }
}