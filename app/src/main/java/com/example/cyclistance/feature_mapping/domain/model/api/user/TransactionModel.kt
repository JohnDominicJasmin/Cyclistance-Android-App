package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionModel(
    val role: String = "",
    val transactionId: String = ""
):Parcelable
