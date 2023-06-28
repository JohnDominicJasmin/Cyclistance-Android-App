package com.example.cyclistance.feature_message.presentation.messaging_conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_message.domain.model.ui.MessageConversation
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MessagingConversationState(
    val conversation: MessageConversation = MessageConversation()
) : Parcelable
