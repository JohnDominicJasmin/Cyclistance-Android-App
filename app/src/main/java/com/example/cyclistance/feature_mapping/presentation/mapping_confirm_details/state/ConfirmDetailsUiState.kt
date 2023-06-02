package com.example.cyclistance.feature_mapping.presentation.mapping_confirm_details.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConfirmDetailsUiState(
    val address: String = "",
    val addressErrorMessage: String = "",
    val message: String = "",
    val bikeType:String = "",
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val isNoInternetVisible: Boolean = false,
    val alertDialogState : AlertDialogState = AlertDialogState()
):Parcelable
