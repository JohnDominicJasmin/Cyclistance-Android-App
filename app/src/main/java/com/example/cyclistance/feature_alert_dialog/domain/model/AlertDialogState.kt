package com.example.cyclistance.feature_alert_dialog.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class AlertDialogState(
    val title: String = "",
    val description: String = "",
    val icon: Int = -1
):Parcelable
