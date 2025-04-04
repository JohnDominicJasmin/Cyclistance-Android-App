package com.myapp.cyclistance.feature_mapping.domain.use_case.user

import com.myapp.cyclistance.feature_mapping.domain.model.remote_models.user.UserItem
import com.myapp.cyclistance.feature_mapping.domain.repository.MappingRepository

class CreateUserUseCase(private val repository: MappingRepository) {
    suspend operator fun invoke(user: UserItem){
        repository.createUser(user)
    }
}