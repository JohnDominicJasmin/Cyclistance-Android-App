package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CancellationDto(
    @SerializedName("cancellation_reason")
    val cancellationReason: CancellationReasonDto = CancellationReasonDto(),
    @SerializedName("id_cancelled_by")
    val idCancelledBy: String = "",
    @SerializedName("name_cancelled_by")
    val nameCancelledBy: String = "",
    @SerializedName("rescue_cancelled")
    val rescueCancelled: Boolean = false
):Parcelable