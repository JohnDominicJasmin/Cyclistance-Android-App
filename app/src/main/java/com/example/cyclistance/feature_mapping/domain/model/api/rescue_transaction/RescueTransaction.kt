package com.example.cyclistance.feature_mapping.domain.model.api.rescue_transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RescueTransaction(
    val transactions: List<RescueTransactionItem> = emptyList()
):Parcelable
