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
):Parcelable{
    fun getPhoto() = userProfileInfo?.photoUrl
    fun getName() = userProfileInfo?.name
    fun getAverageRating() = userProfileInfo?.averageRating
    fun getAddress() = userProfileInfo?.address
    fun getBikeGroup() = userProfileInfo?.bikeGroup
    fun getRequestAssistanceFrequency() = userActivity?.requestAssistanceFrequency
    fun getRescueFrequency() = userActivity?.rescueFrequency
    fun getOverallDistanceOfRescue() = userActivity?.overallDistanceOfRescue
    fun getAverageSpeed() = userActivity?.averageSpeed
    fun getInjuryCount() = reasonAssistance?.injuryCount
    fun getFrameSnapCount() = reasonAssistance?.frameSnapCount
    fun getFlatTireCount() = reasonAssistance?.flatTireCount
    fun getBrokenChainCount() = reasonAssistance?.brokenChainCount
    fun getIncidentCount() = reasonAssistance?.incidentCount
    fun getFaultyBrakesCount() = reasonAssistance?.faultyBrakesCount

}