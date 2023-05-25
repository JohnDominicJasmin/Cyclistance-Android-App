package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RescueRequestDto(
    @SerializedName("respondents")
    val respondents: List<RespondentDto> = emptyList()
):Parcelable