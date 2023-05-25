package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CancellationReasonDto(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("reason")
    val reason: String = ""
):Parcelable