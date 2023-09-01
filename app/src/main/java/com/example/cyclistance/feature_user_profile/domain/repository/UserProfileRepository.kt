package com.example.cyclistance.feature_user_profile.domain.repository

import com.example.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.example.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel

interface UserProfileRepository {
    suspend fun updateProfile(photoUri: String?, name: String?): Boolean
    suspend fun uploadImage(v: String): String
    suspend fun getName(): String?
    suspend fun getPhotoUrl(): String?

    suspend fun updateUserActivity(id: String, userActivity: UserActivityModel)
    suspend fun updateUserProfileInfo(id: String, userProfile: UserProfileInfoModel)
    suspend fun updateReasonAssistance(id: String, reasonAssistanceModel: ReasonAssistanceModel)

    suspend fun getUserProfile(id:String): UserProfileModel
}