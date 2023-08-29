package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class UserActivityModel(
    val requestAssistanceFrequency: Int,
    val rescueFrequency: Int,
    val overallDistanceOfRescue: Int,
    val averageSpeed: Int
):Parcelable


