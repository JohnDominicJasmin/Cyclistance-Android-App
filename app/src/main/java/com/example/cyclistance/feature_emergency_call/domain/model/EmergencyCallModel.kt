package com.example.cyclistance.feature_emergency_call.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class EmergencyCallModel(
    val contacts: List<EmergencyContactModel> = emptyList()
) : Parcelable
