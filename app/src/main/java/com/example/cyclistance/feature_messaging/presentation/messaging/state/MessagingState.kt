package com.example.cyclistance.feature_messaging.presentation.messaging.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.MessageConversation
import com.example.cyclistance.feature_messaging.domain.model.ui.MessagesModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MessagingState(
    val conversation: MessageConversation = MessageConversation(),
    val messagesModel: MessagesModel = MessagesModel()

) : Parcelable
