package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState


@Composable
internal fun ChatsSection(
    modifier: Modifier = Modifier,
    state: ChatState,
    isInternetAvailable: Boolean,
    chatState: List<Pair<MessagingUserItemModel,ChatItemModel>>,
    onClick: (MessagingUserItemModel) -> Unit) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()) {
        items(chatState, key = {
            it.second.messageId
        }) { item ->

            ChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                isInternetAvailable = isInternetAvailable,
                chatItem = item.second,
                user = item.first,
                chatState = state,
                onClick = onClick)

        }
    }
}