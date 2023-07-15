package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class EmergencyCallUIState(
    val deleteDialogVisible: Boolean = false,
    val contactToDelete: EmergencyContactModel = EmergencyContactModel(),
    val selectedPhoneNumber: String = "",
    val maximumContactDialogVisible: Boolean = false,

    val selectedImageUri: String = "",
    val name: @RawValue TextFieldValue = TextFieldValue(""),
    val nameErrorMessage: String = "",
    val phoneNumber: @RawValue TextFieldValue = TextFieldValue(""),
    val phoneNumberErrorMessage: String = "",
    val contactCurrentlyEditing: EmergencyContactModel? = null,
) : Parcelable
