package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class AddEditContactState(
    val isLoading: Boolean = false,
    val nameSnapshot: String = "",
    val phoneNumberSnapshot: String = "",
    val emergencyContact: EmergencyContactModel? = null,
    val editingContactId: Int = -1
) : Parcelable

