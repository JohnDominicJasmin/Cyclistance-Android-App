package com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.state

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
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

