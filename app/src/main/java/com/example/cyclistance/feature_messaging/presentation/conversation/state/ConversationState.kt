package com.example.cyclistance.feature_messaging.presentation.conversation.state

import android.os.Parcelable
import com.example.cyclistance.core.utils.annotations.StableState
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import kotlinx.parcelize.Parcelize


@Parcelize
@StableState
data class ConversationState(
    val messageUser: MessagingUserItemModel? = null,
    val userUid: String = "",
    val userName: String = "",
    val userPhoto: String = "",
    val conversionId: String? = null,
    val isLoading: Boolean = false,
) : Parcelable
