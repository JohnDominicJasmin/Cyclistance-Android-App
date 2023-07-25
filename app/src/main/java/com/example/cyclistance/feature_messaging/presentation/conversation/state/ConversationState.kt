package com.example.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationsModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConversationState(
    val conversationsModel: ConversationsModel = ConversationsModel(),
    val chatName: String = "",
    val chatId: String = "",
    val chatPhotoUrl: String = "",
) : Parcelable
