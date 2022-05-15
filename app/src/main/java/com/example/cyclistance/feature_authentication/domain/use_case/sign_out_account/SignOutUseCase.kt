package com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class SignOutUseCase(private val repository: AuthRepository<AuthCredential>) {

     operator fun invoke() = repository.signOut()
}