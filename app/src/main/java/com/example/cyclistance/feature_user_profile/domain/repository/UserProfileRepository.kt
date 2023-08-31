package com.example.cyclistance.feature_user_profile.domain.repository

import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel

interface UserProfileRepository {
    suspend fun updateProfile(photoUri: String?, name: String?): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun getName(): String?
    suspend fun getPhotoUrl(): String?

    suspend fun updateUserProfileInfo(userProfile: UserProfileInfoModel)

    suspend fun getUserProfileInfo(id:String): UserProfileModel
}