package com.example.cyclistance.feature_authentication.data.mapper

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

object AuthResultMapper {
    fun Task<AuthResult>.toAuthenticationResult(): AuthenticationResult {
        val user = this.result?.user
        return AuthenticationResult(
            isSuccessful = this.isSuccessful,
            user = UserDetails(
                uid = user?.uid ?: "",
                name = user?.displayName ?: "",
                photo = user?.photoUrl.toString(),
                email = user?.email ?: ""
            )
        )
    }
}