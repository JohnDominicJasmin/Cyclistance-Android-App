package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository

class GetEmailUseCase(private val repository: AuthRepository){
     operator fun invoke():String? {
        return repository.getEmail()
     }
}