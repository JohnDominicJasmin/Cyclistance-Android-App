package com.example.cyclistance.feature_mapping.data.remote.dto.user_dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class UserAssistance(
    @SerializedName("confirmationDetail")
    val confirmationDetail: ConfirmationDetail = ConfirmationDetail(),
    @SerializedName("need_help")
    val needHelp: Boolean = false
):Parcelable