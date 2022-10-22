package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Cancellation(
    @SerializedName("cancellation_reason")
    val cancellationReason: CancellationReason = CancellationReason(),
    @SerializedName("rescue_cancelled")
    val rescueCancelled: Boolean = false,
    @SerializedName("rescue_cancelled_by")
    val rescueCancelledBy: String = ""
):Parcelable