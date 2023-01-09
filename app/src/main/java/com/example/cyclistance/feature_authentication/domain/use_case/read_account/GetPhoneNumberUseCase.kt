package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import kotlinx.coroutines.flow.first

class GetPhoneNumberUseCase(private val repository: AuthRepository<*>) {
    suspend operator fun invoke(): String {
        return repository.getPhoneNumber().first().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.PhoneNumberException()
    }
}