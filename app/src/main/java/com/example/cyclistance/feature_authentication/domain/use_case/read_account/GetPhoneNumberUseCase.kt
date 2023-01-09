package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.google.firebase.auth.AuthCredential
import kotlinx.coroutines.flow.first

class GetPhoneNumberUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {
    suspend operator fun invoke(): String {
        return repository.getPhoneNumber().first().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.PhoneNumberException()
    }
}