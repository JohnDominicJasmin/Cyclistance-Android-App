package com.example.cyclistance.feature_messaging.domain.model

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import kotlinx.parcelize.Parcelize

@StableState
@Parcelize
data class SendNotificationModel(
    val userReceiverMessage: MessagingUserItemModel = MessagingUserItemModel(),
    val userSenderMessage: MessagingUserItemModel = MessagingUserItemModel(),
    val senderName: String = "",
    val message: String = "",
):Parcelable
