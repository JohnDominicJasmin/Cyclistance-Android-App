package com.example.cyclistance.feature_user_profile.data.mapper

import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_ADDRESS
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_BIKE_GROUP
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_ACTIVITY
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_RATINGS
import com.example.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_REASON_ASSISTANCE
import com.example.cyclistance.core.utils.constants.UtilConstants
import com.example.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.example.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.example.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.example.cyclistance.feature_user_profile.domain.model.UserProfileModel
import com.google.firebase.firestore.DocumentSnapshot

object UserProfileInfoMapper {

    fun DocumentSnapshot.toUserProfileInfo(): UserProfileModel {
        val userActivityObject = get(KEY_USER_ACTIVITY) as? Map<*, *>
        val reasonAssistanceObject = get(KEY_USER_REASON_ASSISTANCE) as? Map<*, *>
        val requestAssistanceFrequency = (userActivityObject?.get("requestAssistanceFrequency") as? Long)?.toInt() ?: 0
        val rescueFrequency = (userActivityObject?.get("rescueFrequency") as? Long)?.toInt() ?: 0
        val overallDistanceOfRescue = (userActivityObject?.get("overallDistanceOfRescue") as? Long)?.toInt() ?: 0
        val averageSpeed = (userActivityObject?.get("averageSpeed") as? Long)?.toInt() ?: 0
        val injuryCount = (reasonAssistanceObject?.get("injuryCount") as? Long)?.toInt() ?: 0
        val frameSnapCount = (reasonAssistanceObject?.get("frameSnapCount") as? Long)?.toInt() ?: 0
        val flatTireCount = (reasonAssistanceObject?.get("flatTireCount") as? Long)?.toInt() ?: 0
        val brokenChainCount = (reasonAssistanceObject?.get("brokenChainCount") as? Long)?.toInt() ?: 0
        val incidentCount = (reasonAssistanceObject?.get("incidentCount") as? Long)?.toInt() ?: 0
        val faultyBrakesCount = (reasonAssistanceObject?.get("faultyBrakesCount") as? Long)?.toInt() ?: 0

        @Suppress("UNCHECKED_CAST")
        val ratings:List<Int> = get(KEY_USER_RATINGS) as? List<Int> ?: emptyList()
        val averageRating = ratings.average()

        return UserProfileModel(
            userProfileInfo = UserProfileInfoModel(
                photoUrl = getString(UtilConstants.KEY_PHOTO) ?: "",
                address = getString(KEY_ADDRESS) ?: "",
                averageRating = averageRating,
                name = getString(KEY_NAME) ?: "",
                bikeGroup = getString(KEY_BIKE_GROUP) ?: ""
            ),
            userActivity = UserActivityModel(
                requestAssistanceFrequency = requestAssistanceFrequency,
                rescueFrequency = rescueFrequency,
                overallDistanceOfRescue = overallDistanceOfRescue,
                averageSpeed = averageSpeed
            ),
            reasonAssistance = ReasonAssistanceModel(
                injuryCount = injuryCount,
                frameSnapCount = frameSnapCount,
                flatTireCount = flatTireCount,
                brokenChainCount = brokenChainCount,
                incidentCount = incidentCount,
                faultyBrakesCount = faultyBrakesCount
            )
        )
    }
}