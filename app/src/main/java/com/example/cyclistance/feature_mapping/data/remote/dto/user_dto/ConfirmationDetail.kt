package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConfirmationDetailDto(
    @SerializedName("bike_type")
    val bikeType: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("message")
    val message: String = ""
):Parcelable