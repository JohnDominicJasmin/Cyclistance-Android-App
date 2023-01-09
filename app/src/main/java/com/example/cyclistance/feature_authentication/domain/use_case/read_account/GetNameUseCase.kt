package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions

class GetNameUseCase(private val repository: AuthRepository<*>) {

     operator fun invoke(): String {

        return repository.getName().takeIf { !it.isNullOrEmpty() }
             ?: throw MappingExceptions.NameException()

    }
}