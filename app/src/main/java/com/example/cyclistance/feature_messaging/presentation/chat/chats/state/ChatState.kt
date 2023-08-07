package com.example.cyclistance.feature_messaging.presentation.chat.chats.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import kotlinx.parcelize.Parcelize

@Parcelize
@StableState
data class ChatState(
    val messageUserInfo: MessagingUserItemModel? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
) : Parcelable
