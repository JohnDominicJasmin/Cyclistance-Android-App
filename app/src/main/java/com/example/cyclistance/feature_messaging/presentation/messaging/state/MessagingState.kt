package com.example.cyclistance.feature_messaging.presentation.messaging.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessagesModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MessagingState(
    val conversationsModel: ConversationsModel = ConversationsModel(),
    val messagesModel: MessagesModel = MessagesModel()

) : Parcelable
