package com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Cancellation(
    @SerializedName("cancellation_reason")
    val cancellationReason: CancellationReason = CancellationReason(),
    @SerializedName("id_cancelled_by")
    val idCancelledBy: String = "",
    @SerializedName("rescue_cancelled")
    val rescueCancelled: Boolean = false
):Parcelable