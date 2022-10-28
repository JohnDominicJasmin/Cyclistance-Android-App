package com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RescueTransactionDto(
    @SerializedName("cancellation")
    val cancellation: Cancellation = Cancellation(),
    @SerializedName("id")
    val id: String = "",
    @SerializedName("rescuee_id")
    val rescueeId: String = "",
    @SerializedName("rescuer_id")
    val rescuerId: String = "",
    @SerializedName("status")
    val status: Status = Status()
):Parcelable