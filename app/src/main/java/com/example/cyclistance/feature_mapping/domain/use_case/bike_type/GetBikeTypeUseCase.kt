package com.example.cyclistance.feature_mapping.domain.use_case.bike_type

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow


class GetBikeTypeUseCase(private val repository: MappingRepository) {

    suspend operator fun invoke(): Flow<String> {
        return repository.getBikeType()
    }

}