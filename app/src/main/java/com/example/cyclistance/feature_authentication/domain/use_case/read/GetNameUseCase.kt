package com.example.cyclistance.feature_authentication.domain.use_case.read

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetNameUseCase(private val repository: AuthRepository<AuthCredential>) {

     operator fun invoke():String?{
      return repository.getName()
    }
}