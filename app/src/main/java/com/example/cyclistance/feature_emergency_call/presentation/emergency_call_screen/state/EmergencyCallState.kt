package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class EmergencyCallState(
    val isLoading: Boolean = false,
    val emergencyCallModel: EmergencyCallModel = EmergencyCallModel()
) : Parcelable