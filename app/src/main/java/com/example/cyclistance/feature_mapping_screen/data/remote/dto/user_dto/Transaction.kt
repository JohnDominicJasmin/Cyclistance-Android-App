package com.example.cyclistance.feature_mapping_screen.data.remote.dto.user_dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Transaction(
    @SerializedName("role")
    val role: String = "",
    @SerializedName("transaction_id")
    val transactionId: String = ""
):Parcelable