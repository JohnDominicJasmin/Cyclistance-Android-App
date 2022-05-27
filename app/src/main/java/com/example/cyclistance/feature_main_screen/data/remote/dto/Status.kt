package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

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