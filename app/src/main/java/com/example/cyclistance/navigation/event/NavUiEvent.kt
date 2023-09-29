package com.example.cyclistance.navigation.event

import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class NavUiEvent{
    data class OnChangeNavigation(val isNavigating:Boolean): NavUiEvent()
    data object OnToggleTheme: NavUiEvent()
    data class NewConversationDetails(val messageUser: MessagingUserItemModel): NavUiEvent()
    data class ChangeEditMode(val isEditMode:Boolean): NavUiEvent()
}
