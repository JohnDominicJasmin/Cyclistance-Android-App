package com.example.cyclistance.feature_messaging.presentation.chat.chats.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState

@Composable
fun SearchUserSection(
    modifier: Modifier = Modifier,
    searchQuery: TextFieldValue,
    state: ChatState,
    event: (MessagingUiEvent) -> Unit) {

    val filteredQuery = remember(searchQuery.text, state.messagingUsers.users) {

        state.messagingUsers.users.filter {
            it.userDetails.name.contains(searchQuery.text, ignoreCase = true)
        }

    }

    val resultFound = remember(filteredQuery) { filteredQuery.isNotEmpty() }

    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center) {

        if (!resultFound) {
            SearchMessageNotFound()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = filteredQuery, key = { it.userDetails.uid }) { model ->
                    ChatSearchItem(
                        modifier = Modifier.fillMaxWidth(),
                        messageUser = model,
                        onClick = {
                            event(MessagingUiEvent.OnSelectConversation(model))
                        }
                    )
                }
            }
        }

    }
}