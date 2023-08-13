package com.example.cyclistance.feature_authentication.presentation.auth_email.state

import android.os.Parcelable
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class EmailAuthUiState(
    val isNoInternetVisible: Boolean = false,
    val isTimerRunning: Boolean = false,
    val alertDialogState: AlertDialogState = AlertDialogState()
):Parcelable
