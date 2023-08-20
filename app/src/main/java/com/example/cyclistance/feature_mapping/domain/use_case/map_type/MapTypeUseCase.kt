package com.example.cyclistance.feature_mapping.domain.use_case.map_type

import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository

class MapTypeUseCase(private val repository: MappingUiStoreRepository) {
    suspend operator fun invoke() = repository.getMapType()
    suspend operator fun invoke(mapType: String) {
        repository.setMapType(mapType)
    }
}