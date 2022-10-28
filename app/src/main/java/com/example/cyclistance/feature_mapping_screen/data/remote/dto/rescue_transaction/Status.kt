package com.example.cyclistance.feature_mapping_screen.data.remote.dto.rescue_transaction


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Status(
    @SerializedName("finished")
    val finished: Boolean = false,
    @SerializedName("ongoing")
    val ongoing: Boolean = false,
    @SerializedName("searching")
    val searching: Boolean = false,
    @SerializedName("started")
    val started: Boolean = false
):Parcelable