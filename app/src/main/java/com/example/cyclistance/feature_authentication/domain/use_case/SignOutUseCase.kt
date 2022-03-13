package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.common.Resource
import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignOutUseCase(private val repository: AuthRepository<AuthCredential>) {

     operator fun invoke() = repository.signOut()
}