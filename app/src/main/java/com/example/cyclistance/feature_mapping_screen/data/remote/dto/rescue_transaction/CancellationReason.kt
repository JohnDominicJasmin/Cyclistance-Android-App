package com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CancellationReason(
    @SerializedName("message")
    val message: String = "",
    @SerializedName("reason")
    val reason: String = ""
):Parcelable