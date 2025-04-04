package com.myapp.cyclistance.feature_user_profile.domain.repository

import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileModel

interface UserProfileRepository {
    suspend fun updateProfile(photoUri: String?, name: String?): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun getName(): String?
    suspend fun getPhotoUrl(): String?
    suspend fun updateUserProfileInfo(id: String, userProfile: UserProfileInfoModel)
    suspend fun getUserProfile(id:String): UserProfileModel

}