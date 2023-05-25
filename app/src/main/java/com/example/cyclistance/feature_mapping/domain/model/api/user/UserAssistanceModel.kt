package com.example.cyclistance.feature_mapping.domain.model.api.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserAssistanceModel(
    val confirmationDetail: ConfirmationDetailModel = ConfirmationDetailModel(),
    val needHelp: Boolean = false
):Parcelable
