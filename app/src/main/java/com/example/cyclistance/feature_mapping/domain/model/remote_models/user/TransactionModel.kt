package com.example.cyclistance.feature_mapping.domain.model.remote_models.user

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class TransactionModel(
    val role: String = "",
    val transactionId: String = ""
):Parcelable
