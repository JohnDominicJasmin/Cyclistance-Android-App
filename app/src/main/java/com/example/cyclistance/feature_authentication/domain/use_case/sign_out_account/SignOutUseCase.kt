package com.example.cyclistance.feature_authentication.domain.use_case.sign_out_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class SignOutUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {

     operator fun invoke() = repository.signOut()
}