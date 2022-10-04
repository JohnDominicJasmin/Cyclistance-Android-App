package com.example.cyclistance.feature_mapping_screen.domain.use_case.address

import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow

class GetAddressUseCase(private val repository: MappingRepository) {
     operator fun invoke(): Flow<String> {
       return repository.getAddress()
    }
}