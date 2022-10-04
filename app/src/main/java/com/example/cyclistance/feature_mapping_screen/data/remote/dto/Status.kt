package com.example.cyclistance.feature_mapping_screen.data.remote.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Status(
    @SerializedName("finished")
    val finished: Boolean = false,
    @SerializedName("ongoing")
    val ongoing: Boolean = false,
    @SerializedName("searching")
    val searching: Boolean = false,
    @SerializedName("started")
    val started: Boolean = false
)