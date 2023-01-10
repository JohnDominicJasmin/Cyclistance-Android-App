package com.example.cyclistance.feature_mapping.domain.use_case.location

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetFullAddressUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(latitude: Double, longitude: Double):String = repository.getFullAddress(
        latitude = latitude,
        longitude = longitude
    )
}