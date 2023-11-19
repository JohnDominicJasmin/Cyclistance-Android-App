package com.myapp.cyclistance.feature_mapping.domain.model.remote_models.rescue_transaction

import android.os.Parcelable
import com.myapp.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class CancellationModel(
    val cancellationReason: CancellationReasonModel = CancellationReasonModel(),
    val idCancelledBy: String = "",
    val nameCancelledBy: String = "",
    val rescueCancelled: Boolean = false
):Parcelable
