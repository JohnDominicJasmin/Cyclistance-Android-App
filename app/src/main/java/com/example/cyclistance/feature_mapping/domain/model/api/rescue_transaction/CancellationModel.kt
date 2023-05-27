package com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class CancellationModel(
    val cancellationReason: CancellationReasonModel = CancellationReasonModel(),
    val idCancelledBy: String = "",
    val nameCancelledBy: String = "",
    val rescueCancelled: Boolean = false
):Parcelable
