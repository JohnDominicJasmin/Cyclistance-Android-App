package com.example.cyclistance.feature_authentication.domain.use_case

import com.example.cyclistance.feature_authentication.domain.repository.AuthRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReloadEmailUseCase(private val repository: AuthRepository<AuthCredential>) {

    suspend operator fun invoke():Boolean = repository.reloadEmail()

}