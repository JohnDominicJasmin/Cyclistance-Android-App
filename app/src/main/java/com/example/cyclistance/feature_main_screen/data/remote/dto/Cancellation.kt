package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Cancellation(
    @SerializedName("cancellation_reason")
    val cancellationReason: CancellationReason = CancellationReason(),
    @SerializedName("rescue_cancelled")
    val rescueCancelled: Boolean = false,
    @SerializedName("rescue_cancelled_by")
    val rescueCancelledBy: String = ""
)