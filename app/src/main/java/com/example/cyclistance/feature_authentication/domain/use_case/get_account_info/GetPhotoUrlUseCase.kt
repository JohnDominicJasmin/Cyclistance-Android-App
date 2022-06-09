package com.example.cyclistance.feature_authentication.domain.use_case.get_account_info

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetPhotoUrlUseCase(private val repository: AuthRepository<AuthCredential>) {
    operator fun invoke(): Uri?{
        return repository.getPhotoUrl()
    }
}