package com.example.cyclistance.feature_main_screen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class Status(
    @SerializedName("finished")
    val finished: Boolean,
    @SerializedName("ongoing")
    val ongoing: Boolean,
    @SerializedName("searching")
    val searching: Boolean,
    @SerializedName("started")
    val started: Boolean
)