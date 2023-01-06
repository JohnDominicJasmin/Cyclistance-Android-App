package com.example.cyclistance.feature_mapping.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RescueTransaction(
    val rescueTransactions: List<RescueTransactionItem> = emptyList()
):Parcelable
