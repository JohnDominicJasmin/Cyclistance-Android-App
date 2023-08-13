package com.example.cyclistance.feature_authentication.domain.repository

import com.example.cyclistance.core.domain.model.UserDetails
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential

interface AuthRepository {
    suspend fun reloadEmail(): Boolean
    fun signOut()
    fun getEmail(): String?
    suspend fun sendEmailVerification(): Boolean

    fun getId(): String?
    fun isSignedInWithProvider(): Boolean?
    fun isEmailVerified(): Boolean?
    fun hasAccountSignedIn(): Boolean

    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String): AuthenticationResult?

    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithCredential(credential: SignInCredential): AuthenticationResult
    suspend fun createUser(user: UserDetails)

    suspend fun sendPasswordResetEmail(email: String)

    suspend fun changePassword(currentPassword: String, confirmPassword: String)

}