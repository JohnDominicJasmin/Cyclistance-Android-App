package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class AddEditContactState(
    val isLoading: Boolean = false,
    val nameSnapshot: String = "",
    val phoneNumberSnapshot: String = "",
) : Parcelable
