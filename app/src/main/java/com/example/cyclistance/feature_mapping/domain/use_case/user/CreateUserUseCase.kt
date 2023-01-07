package com.example.cyclistance.feature_mapping.domain.use_case.user

import com.example.cyclistance.feature_mapping.domain.model.UserItem
import com.example.cyclistance.feature_mapping.domain.repository.MappingRepository

class CreateUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(user: UserItem){
        repository.createUser(user)
    }
}