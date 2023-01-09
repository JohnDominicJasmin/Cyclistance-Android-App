package com.example.cyclistance.feature_authentication.domain.use_case.read_account

import android.net.Uri
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.firebase.auth.AuthCredential

class GetEmailUseCase(private val repository: AuthRepository<AuthCredential, Uri>){
     operator fun invoke():String? {
      return repository.getEmail()
     }
}