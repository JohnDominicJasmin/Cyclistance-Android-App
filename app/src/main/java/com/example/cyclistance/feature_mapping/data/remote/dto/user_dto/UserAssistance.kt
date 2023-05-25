package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserAssistanceDto(
    @SerializedName("confirmationDetail")
    val confirmationDetail: ConfirmationDetailDto = ConfirmationDetailDto(),
    @SerializedName("need_help")
    val needHelp: Boolean = false
):Parcelable