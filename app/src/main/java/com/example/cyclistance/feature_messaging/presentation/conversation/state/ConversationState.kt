package com.example.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConversationState(
    val conversationsModel: ConversationsModel = ConversationsModel(),
    val conversationName: String = "",
    val conversationUid: String = "",
    val conversationPhotoUrl: String = "",
    val userUid: String = ""
) : Parcelable
