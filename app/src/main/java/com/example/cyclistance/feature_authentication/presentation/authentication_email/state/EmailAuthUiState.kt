package com.example.cyclistance.feature_authentication.presentation.authentication_email.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class EmailAuthUiState(
    val isNoInternetVisible: Boolean = false,
    val isTimerRunning: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState()
):Parcelable
