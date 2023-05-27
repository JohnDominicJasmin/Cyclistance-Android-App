package com.example.cyclistance.feature_mapping.presentation.mapping_rescue_request.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class RescueRequestUiState(
    val alertDialogState: AlertDialogState = AlertDialogState(),
    val isNoInternetAvailable: Boolean = false):Parcelable