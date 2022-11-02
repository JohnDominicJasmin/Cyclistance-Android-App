package com.example.cyclistance.feature_mapping_screen.domain.use_case.user

import com.example.cyclistance.feature_mapping_screen.domain.model.User
import com.example.cyclistance.feature_mapping_screen.domain.repository.MappingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUsersUseCase(private val repository: MappingRepository) {
    operator fun invoke(): Flow<User> = flow{
        emit(repository.getUsers())
    }
}