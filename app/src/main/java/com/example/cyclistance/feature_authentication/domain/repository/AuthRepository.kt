package com.example.cyclistance.feature_authentication.domain.repository

import com.example.cyclistance.feature_authentication.domain.model.AuthenticationResult
import com.example.cyclistance.feature_authentication.domain.model.AuthenticationUser
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

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthenticationResult?
    suspend fun signInWithCredential(credential: SignInCredential): Boolean
    suspend fun createUser(authUser: AuthenticationUser)

}