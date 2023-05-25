package com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.parcelize.Parcelize

@Parcelize
@Immutable
@Stable
data class RescueTransactionItem(
    val id: String? = null,
    val cancellation: CancellationModel? = null,
    val rescueeId: String? = null,
    val rescuerId: String? = null,
    val status: StatusModel? = null,
    val route: RouteModel? = null
):Parcelable
