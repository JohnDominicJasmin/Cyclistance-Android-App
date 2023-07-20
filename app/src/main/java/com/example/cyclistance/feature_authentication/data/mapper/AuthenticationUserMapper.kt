package com.example.cyclistance.feature_authentication.data.mapper

import com.example.cyclistance.feature_authentication.domain.model.AuthenticationUser
import com.google.firebase.auth.FirebaseUser

object AuthenticationUserMapper {
    fun FirebaseUser.toAuthenticationUser(): AuthenticationUser {
        return AuthenticationUser(
            uid = this.uid,
            name = this.displayName ?: "",
            photo = this.photoUrl.toString(),
            email = this.email ?: ""
        )
    }
}