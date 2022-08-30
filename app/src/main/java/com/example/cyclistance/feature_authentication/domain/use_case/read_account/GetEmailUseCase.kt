package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetEmailUseCase(private val repository: AuthRepository<AuthCredential>){
     operator fun invoke():String? {
      return repository.getEmail()
     }
}