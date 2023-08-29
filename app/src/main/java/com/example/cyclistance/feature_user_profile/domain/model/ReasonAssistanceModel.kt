package com.example.cyclistance.feature_user_profile.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@StableState
@Parcelize
data class ReasonAssistanceModel(
    val injuryCount: Int,
    val frameSnapCount: Int,
    val flatTireCount: Int,
    val brokenChainCount: Int,
    val incidentCount: Int,
    val faultyBrakesCount: Int
):Parcelable