package com.example.cyclistance.feature_messaging.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class SendNotificationModel(
    val receiverToken: String = "",
    val senderName: String = "",
    val message: String = "",
):Parcelable
