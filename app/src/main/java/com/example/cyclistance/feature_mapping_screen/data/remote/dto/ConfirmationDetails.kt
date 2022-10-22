package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConfirmationDetails(
    @SerializedName("bike_type")
    val bikeType: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("message")
    val message: String = ""
):Parcelable