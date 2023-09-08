package com.example.cyclistance.feature_mapping.data.data_source.network.dto.user_dto

import com.google.gson.annotations.SerializedName

data class UserAssistanceDto(
    @SerializedName("confirmationDetail")
    val confirmationDetail: ConfirmationDetailDto = ConfirmationDetailDto(),
    @SerializedName("need_help")
    val needHelp: Boolean = false
)