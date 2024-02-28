package com.myapp.cyclistance.feature_user_profile.data.mapper

import com.google.firebase.firestore.DocumentSnapshot
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_ADDRESS
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_BIKE_GROUP
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_ACTIVITY
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_RATINGS
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.KEY_USER_REASON_ASSISTANCE
import com.myapp.cyclistance.core.utils.constants.UtilConstants
import com.myapp.cyclistance.core.utils.constants.UtilConstants.KEY_NAME
import com.myapp.cyclistance.core.utils.formatter.FormatterUtils.metersToKilometerPerHour
import com.myapp.cyclistance.feature_user_profile.domain.model.ReasonAssistanceModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserActivityModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileModel

object UserProfileInfoMapper {

    fun DocumentSnapshot.toUserProfileInfo(): UserProfileModel {
        val userActivityObject = get(KEY_USER_ACTIVITY) as? Map<*, *>
        val reasonAssistanceObject = get(KEY_USER_REASON_ASSISTANCE) as? Map<*, *>
        val requestAssistanceFrequency = (userActivityObject?.get("requestAssistanceFrequency") as? Long)?.toInt() ?: 0
        val rescueFrequency = (userActivityObject?.get("rescueFrequency") as? Long)?.toInt() ?: 0
        val overallDistanceOfRescue = (userActivityObject?.get("overallDistanceOfRescueInMeters") as? Double) ?: 0.0
        val averageSpeeds = (userActivityObject?.get("averageSpeeds") as? List<Double>)
        val injuryCount = (reasonAssistanceObject?.get("injuryCount") as? Long)?.toInt() ?: 0
        val frameSnapCount = (reasonAssistanceObject?.get("frameSnapCount") as? Long)?.toInt() ?: 0
        val flatTireCount = (reasonAssistanceObject?.get("flatTireCount") as? Long)?.toInt() ?: 0
        val brokenChainCount = (reasonAssistanceObject?.get("brokenChainCount") as? Long)?.toInt() ?: 0
        val incidentCount = (reasonAssistanceObject?.get("incidentCount") as? Long)?.toInt() ?: 0
        val faultyBrakesCount = (reasonAssistanceObject?.get("faultyBrakesCount") as? Long)?.toInt() ?: 0

        @Suppress("UNCHECKED_CAST")
        val ratings:List<Float> = get(KEY_USER_RATINGS) as? List<Float> ?: emptyList()
        val averageRating = if(ratings.isEmpty()) 0.0 else ratings.average()
        val totalRides = averageSpeeds?.size?.toDouble() ?: 0.0
        val totalSpeed = averageSpeeds?.sum() ?: 0.0
        val averageSpeedMps = (totalSpeed / totalRides).takeIf { !it.isNaN() } ?: 0.0
        val averageSpeedKph = metersToKilometerPerHour(averageSpeedMps)

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
                overallDistanceOfRescueInMeters = overallDistanceOfRescue,
                averageSpeed = averageSpeedKph
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