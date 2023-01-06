package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RescueTransactionItemDto(
    @SerializedName("cancellation")
    val cancellation: Cancellation?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("rescuee_id")
    val rescueeId: String?,
    @SerializedName("rescuer_id")
    val rescuerId: String?,
    @SerializedName("status")
    val status: Status?,
    @SerializedName("route")
    val route: Route?
):Parcelable