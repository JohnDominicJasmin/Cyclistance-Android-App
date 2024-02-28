package com.myapp.cyclistance.feature_mapping.domain.use_case.location

import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class GetFullAddressUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(latitude: Double?, longitude: Double?): String? {
        latitude ?: return null
        longitude ?: return null
        return repository.getFullAddress(
            latitude = latitude,
            longitude = longitude
        )
    }
}