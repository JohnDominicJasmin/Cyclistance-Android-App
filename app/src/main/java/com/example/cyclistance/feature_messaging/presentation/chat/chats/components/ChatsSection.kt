package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatsModel


@Composable
internal fun MessagesSection(
    modifier: Modifier = Modifier,
    chatsModel: ChatsModel,
    onClick: (ChatItemModel) -> Unit) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()) {
        items(chatsModel.messages, key = {
            it.messageId
        }) { item ->

            ChatItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                chatItemModel = item,
                onClick = onClick
            )
        }
    }
}