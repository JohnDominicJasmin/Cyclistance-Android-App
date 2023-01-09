package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class IsSignedInWithProviderUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {

     operator fun invoke(): Boolean? {
        return repository.isSignedInWithProvider()
    }


}