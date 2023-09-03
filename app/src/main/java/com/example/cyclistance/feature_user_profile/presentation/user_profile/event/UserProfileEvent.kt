package com.example.cyclistance.feature_user_profile.presentation.user_profile.event

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class UserProfileEvent {
    data class LoadConversationSuccess(
        val userSenderMessage: MessagingUserItemModel,
        val userReceiverMessage: MessagingUserItemModel) : UserProfileEvent()
}
