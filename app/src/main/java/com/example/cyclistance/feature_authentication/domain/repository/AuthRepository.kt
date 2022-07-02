package com.example.cyclistance.feature_authentication.domain.repository

import android.net.Uri
import com.example.cyclistance.utils.SavePreferences
import com.google.firebase.auth.UserInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository<T>:SavePreferences<String> {
    suspend fun reloadEmail(): Boolean
    fun signOut()
    fun getEmail(): String?
    suspend fun sendEmailVerification(): Boolean
    fun getName(): String?
    fun getId(): String?

    fun getPhotoUrl(): String?
    fun isSignedInWithProvider(): Flow<Boolean>
    fun isEmailVerified(): Boolean?
    fun hasAccountSignedIn(): Boolean
    suspend fun uploadImage(uri: Uri): Uri
    suspend fun createUserWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithEmailAndPassword(email: String, password: String): Boolean
    suspend fun signInWithCredentials(v: T): Boolean
    suspend fun updateProfilePicture(photoUri: Uri?, name: String?): Boolean
    }