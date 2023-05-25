package com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CancellationModel(
    val cancellationReason: CancellationReasonModel = CancellationReasonModel(),
    val idCancelledBy: String = "",
    val nameCancelledBy: String = "",
    val rescueCancelled: Boolean = false
):Parcelable
