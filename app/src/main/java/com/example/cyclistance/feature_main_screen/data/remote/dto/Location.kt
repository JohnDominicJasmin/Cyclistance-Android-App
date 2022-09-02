package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Location(
    @SerializedName("lat")
    val lat: String = "",
    @SerializedName("lng")
    val lng: String = ""
)