package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.model.NearbyCyclist
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<NearbyCyclist> = flow{
        emit(repository.getUsers())
    }
}