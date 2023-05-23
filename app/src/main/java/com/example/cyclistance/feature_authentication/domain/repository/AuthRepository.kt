package com.example.cyclistance.feature_authentication.domain.repository

import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun reloadEmail(): Boolean
    fun signOut()
    fun getEmail(): String?
    suspend fun sendEmailVerification(): Boolean
    suspend fun getName(): String?
    fun getId(): String?
    suspend fun getPhotoUrl(): String?
    fun isSignedInWithProvider(): Boolean?
    fun isEmailVerified(): Boolean?
    fun hasAccountSignedIn(): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithCredential(credential: SignInCredential): Boolean
    suspend fun updateProfile(s: String?, name: String?): Boolean
    suspend fun updatePhoneNumber(phoneNumber: String)
    fun getPhoneNumber(): Flow<String>
}