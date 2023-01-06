package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RescueRequest(
    @SerializedName("respondents")
    val respondents: List<Respondent> = emptyList()
):Parcelable