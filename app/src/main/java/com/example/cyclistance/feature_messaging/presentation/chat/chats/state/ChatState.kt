package com.example.cyclistance.feature_messaging.presentation.chat.chats.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatsModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class ChatState(
    val chatsModel: ChatsModel = ChatsModel(),
    val isLoading: Boolean = false
) : Parcelable
