package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions

class GetIdUseCase(private val repository: AuthRepository) {
    operator fun invoke():String {
        return repository.getId().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.UserException(message = "Id not found!")
    }
}