package com.myapp.cyclistance.feature_mapping.data.data_source.network.dto.user_dto


import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("role")
    val role: String = "",
    @SerializedName("transaction_id")
    val transactionId: String = ""
)