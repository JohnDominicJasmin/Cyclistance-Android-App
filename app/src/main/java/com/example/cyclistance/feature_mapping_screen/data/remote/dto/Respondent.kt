package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Respondent(
    @SerializedName("cancellation")
    val cancellation: Cancellation = Cancellation(),
    @SerializedName("client_id")
    val clientId: String = "",
    @SerializedName("request_accepted")
    val requestAccepted: Boolean = false
):Parcelable