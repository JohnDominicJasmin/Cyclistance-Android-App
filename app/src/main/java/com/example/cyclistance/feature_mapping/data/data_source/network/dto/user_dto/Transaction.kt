package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class TransactionDto(
    @SerializedName("role")
    val role: String = "",
    @SerializedName("transaction_id")
    val transactionId: String = ""
)