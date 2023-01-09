package com.example.cyclistance.feature_authentication.domain.use_case.verify_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class ReloadEmailUseCase(private val repository: AuthRepository<AuthCredential, Uri>) {

    suspend operator fun invoke():Boolean = repository.reloadEmail()

}