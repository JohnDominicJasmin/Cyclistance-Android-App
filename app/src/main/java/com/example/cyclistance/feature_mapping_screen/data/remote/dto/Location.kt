package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Location(
    @SerializedName("lat")
    val lat: String = "",
    @SerializedName("lng")
    val lng: String = ""
):Parcelable