package com.example.cyclistance.feature_mapping.data.remote.dto.rescue_transaction


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class StatusDto(
    @SerializedName("finished")
    val finished: Boolean = false,
    @SerializedName("ongoing")
    val ongoing: Boolean = false,
    @SerializedName("started")
    val started: Boolean = false
):Parcelable