package com.example.cyclistance.feature_alert_dialog.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class AlertDialogState(
    val title: String = "",
    val description: String = "",
    val icon: Int = -1
):Parcelable
