package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.example.cyclistance.feature_mapping.domain.exceptions.MappingExceptions
import com.google.firebase.auth.AuthCredential

class GetIdUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {
    operator fun invoke():String {
        return repository.getId().takeIf { !it.isNullOrEmpty() }
               ?: throw MappingExceptions.UserException()
    }
}