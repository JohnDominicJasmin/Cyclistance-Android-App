package com.example.cyclistance.feature_messaging.presentation.conversation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.validation.FormatterUtils.toReadableDateTime
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState

@Composable
fun ConversationChatItems(
    modifier: Modifier = Modifier,
    keyboardIsOpen: Boolean,
    listState: LazyListState,
    conversation: List<ConversationItemModel>,
    userUid: String,
    uiState: ConversationUiState,
    event: (ConversationUiEvent) -> Unit) {


    LaunchedEffect(key1 = true, conversation.size, keyboardIsOpen) {
        listState.scrollToItem(conversation.indices.last)
    }


    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)) {

        itemsIndexed(
            items = conversation,
            key = { _, item -> item.messageId }) { index, message ->

            val isSender by remember { derivedStateOf { message.senderId != userUid } }
            val timeStampAvailable by remember { derivedStateOf { message.messageDuration != null && message.timestamp != null } }

            AnimatedVisibility(visible = timeStampAvailable) {

                MessagingTimeStamp(
                    value = message.timestamp!!.toReadableDateTime(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp))
            }

            ChatItem(
                conversation = message,
                isSender = isSender,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 6.dp),
                contentAlignment = if (isSender) Alignment.CenterStart else Alignment.CenterEnd,
                currentIndex = index,
                selectedIndex = uiState.chatItemSelectedIndex,
                onSelectChatMessage = {
                    event(
                        ConversationUiEvent.SelectChatItem(
                            index = it))
                }
            )
        }

    }
}