package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class UserProfileModel(
    val userProfileInfo: UserProfileInfoModel? = null,
    val userActivity: UserActivityModel? = null,
    val reasonAssistance: ReasonAssistanceModel? = null
) : Parcelable {
    fun getPhoto(): String = userProfileInfo?.photoUrl ?: ""
    fun getName(): String = userProfileInfo?.name ?: ""
    fun getAverageRating(): Double = userProfileInfo?.averageRating ?: 0.0
    fun getAddress(): String? = userProfileInfo?.address ?: ""
    fun getBikeGroup(): String? = userProfileInfo?.bikeGroup ?: ""
    fun getRequestAssistanceFrequency(): Int = userActivity?.requestAssistanceFrequency ?: 0
    fun getRescueFrequency(): Int = userActivity?.rescueFrequency ?: 0
    fun getOverallDistanceInMeters(): Double = userActivity?.overallDistanceOfRescueInMeters ?: 0.0
    fun getAverageSpeed(): Double = userActivity?.averageSpeed ?: 0.0
    fun getInjuryCount(): Int = reasonAssistance?.injuryCount ?: 0
    fun getFrameSnapCount(): Int = reasonAssistance?.frameSnapCount ?: 0
    fun getFlatTireCount(): Int = reasonAssistance?.flatTireCount ?: 0
    fun getBrokenChainCount(): Int = reasonAssistance?.brokenChainCount ?: 0
    fun getIncidentCount(): Int = reasonAssistance?.incidentCount ?: 0
    fun getFaultyBrakesCount(): Int = reasonAssistance?.faultyBrakesCount ?: 0

}