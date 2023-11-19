package com.myapp.cyclistance.feature_mapping.presentation.mapping_confirm_details.state

import android.os.Parcelable
import com.myapp.cyclistance.core.domain.model.AlertDialogState
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConfirmDetailsUiState(
    val addressErrorMessage: String = "",
    val bikeTypeErrorMessage: String = "",
    val description: String = "",
    val descriptionErrorMessage: String = "",
    val isNoInternetVisible: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val backgroundLocationPermissionDialogVisible: Boolean = false,
    val foregroundLocationPermissionDialogVisible: Boolean = false,
):Parcelable
