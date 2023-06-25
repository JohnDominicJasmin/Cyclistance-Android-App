package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state

import android.os.Parcelable
import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@StableState
data class ConfirmDetailsUiState(
    val address: @RawValue TextFieldValue = TextFieldValue(""),
    val addressErrorMessage: String = "",
    val message: @RawValue TextFieldValue = TextFieldValue(""),
    val bikeType: @RawValue TextFieldValue = TextFieldValue(""),
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val isNoInternetVisible: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val backgroundLocationPermissionDialogVisible: Boolean = false,
    val foregroundLocationPermissionDialogVisible: Boolean = false,
):Parcelable
