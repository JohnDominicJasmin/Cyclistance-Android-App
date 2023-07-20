package com.example.cyclistance.core.data.mapper

import com.example.cyclistance.core.domain.model.UserDetails
import com.google.firebase.auth.FirebaseUser

object UserDetailsMapper {
    fun FirebaseUser.toUserDetails(): UserDetails {
        return UserDetails(
            uid = this.uid,
            name = this.displayName ?: "",
            photo = this.photoUrl.toString(),
            email = this.email ?: ""
        )
    }
}