package com.example.cyclistance.feature_user_profile.domain.repository

import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel

interface UserProfileRepository {
    suspend fun updateProfile(photoUri: String?, name: String?): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun getName(): String?
    suspend fun getPhotoUrl(): String?
    suspend fun upsertUserProfileInfo(userProfile: UserProfileInfoModel)
}