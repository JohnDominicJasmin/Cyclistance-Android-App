package com.example.cyclistance.feature_mapping.domain.use_case.address

import com.example.cyclistance.feature_mapping.domain.repository.MappingUiStoreRepository
import kotlinx.coroutines.flow.Flow

class GetAddressUseCase(private val repository: MappingUiStoreRepository) {
     suspend operator fun invoke(): Flow<String> {
       return repository.getAddress()
    }
}