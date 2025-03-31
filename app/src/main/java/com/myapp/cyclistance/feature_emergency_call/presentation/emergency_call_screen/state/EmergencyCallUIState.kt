package com.myapp.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class EmergencyCallUIState(
    val deleteDialogVisible: Boolean = false,
    val contactToDelete: EmergencyContactModel = EmergencyContactModel(),
    val selectedPhoneNumber: String = "",
    val maximumContactDialogVisible: Boolean = false,
    val callPhonePermissionDialogVisible: Boolean = false,
    val prominentCallPhoneDialogVisible: Boolean = false


) : Parcelable
