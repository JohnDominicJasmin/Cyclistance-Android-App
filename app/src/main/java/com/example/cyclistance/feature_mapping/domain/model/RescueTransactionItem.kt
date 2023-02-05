package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Cancellation
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Route
import com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction.Status
import kotlinx.parcelize.Parcelize
@Parcelize
@Immutable
@Stable
data class RescueTransactionItem(
    val id: String? = null,
    val cancellation: Cancellation? = null,
    val rescueeId: String? = null,
    val rescuerId: String? = null,
    val status: Status? = null,
    val route: Route? = null
):Parcelable
