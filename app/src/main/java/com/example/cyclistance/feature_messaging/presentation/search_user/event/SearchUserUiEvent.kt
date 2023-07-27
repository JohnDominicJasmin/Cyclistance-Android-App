package com.example.cyclistance.feature_messaging.presentation.search_user.event

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel

sealed class SearchUserUiEvent{
    data class OnSelectConversation(val messageUser: MessagingUserItemModel): SearchUserUiEvent()
    object CloseScreen : SearchUserUiEvent()

    data class OnSearchQueryChanged(val searchQuery: TextFieldValue) : SearchUserUiEvent()
    object ClearSearchQuery : SearchUserUiEvent()

}
