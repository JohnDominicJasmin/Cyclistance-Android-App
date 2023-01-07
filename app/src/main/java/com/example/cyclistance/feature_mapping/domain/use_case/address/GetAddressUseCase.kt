package com.example.cyclistance.feature_mapping.domain.use_case.address

import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetAddressUseCase(private val repository: MappingRepository) {
     suspend operator fun invoke(): Flow<String> {
       return repository.getAddress()
    }
}