package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class TransactionDto(
    @SerializedName("role")
    val role: String = "",
    @SerializedName("transaction_id")
    val transactionId: String = ""
):Parcelable