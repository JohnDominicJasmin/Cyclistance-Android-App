package com.myapp.cyclistance.feature_authentication.data.mapper

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.myapp.cyclistance.core.domain.model.UserDetails
import com.myapp.cyclistance.core.utils.constants.MappingConstants.IMAGE_PLACEHOLDER_URL
import com.myapp.cyclistance.feature_authentication.domain.model.AuthenticationResult

object AuthResultMapper {
    fun Task<AuthResult>.toAuthenticationResult(): AuthenticationResult {
        val user = this.result?.user
        return AuthenticationResult(
            isSuccessful = this.isSuccessful,
            user = UserDetails(
                uid = user?.uid ?: "",
                name = user?.displayName ?: "",
                photo = user?.photoUrl.toString().takeIf { it != "null" } ?: IMAGE_PLACEHOLDER_URL,
                email = user?.email ?: ""
            )
        )
    }
}