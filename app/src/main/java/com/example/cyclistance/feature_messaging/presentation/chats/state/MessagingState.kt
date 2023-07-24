package com.example.cyclistance.feature_messaging.presentation.chats.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatsModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class MessagingState(

    val chatsModel: ChatsModel = ChatsModel(),


    ) : Parcelable
