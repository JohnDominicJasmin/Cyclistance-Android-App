package com.example.cyclistance.feature_messaging.presentation.messaging

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.MessageItemModel
import com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list.MessagingScreenContent
import com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.messaging.event.MessagingEvent
import com.example.cyclistance.feature_messaging.presentation.messaging.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingUiState

@Composable
fun MessagingScreen(
    viewModel: MessagingViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    var uiState by rememberSaveable { mutableStateOf(MessagingUiState()) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val onSelectedConversation = remember {
        { _messageItem: MessageItemModel ->
            uiState = uiState.copy(
                selectedConversationItem = _messageItem
            )
        }
    }

    val onDismissConversationDialog = remember {
        {
            uiState = uiState.copy(
                selectedConversationItem = null
            )
        }
    }

    val onToggleExpand = remember {
        {
            uiState = uiState.copy(
                messageAreaExpanded = !uiState.messageAreaExpanded
            )
        }
    }
    val onChangeValueMessage = remember<(TextFieldValue) -> Unit> {
        {
            uiState = uiState.copy(
                message = it
            )
        }
    }

    val onClickChatItem = remember {
        { index: Int ->
            uiState = uiState.copy(
                chatItemSelectedIndex = if (uiState.chatItemSelectedIndex == index) {
                    -1
                } else {
                    index
                })
        }
    }

    val resetSelectedIndex = remember {
        {
            uiState = uiState.copy(
                chatItemSelectedIndex = -1
            )
        }
    }

    val onSendMessage = remember {
        {


            viewModel.onEvent(
                event = MessagingEvent.SendMessage(
                    sendMessageModel = SendMessageModel(
                        receiverId = uiState.selectedConversationItem!!.userId,
                        message = uiState.message.text
                    )
                )).also {
                uiState = uiState.copy(
                    message = TextFieldValue()
                )
            }


        }
    }


    BackHandler(enabled = true, onBack = {
        if (uiState.messageAreaExpanded) {
            onToggleExpand()
        } else {
            navController.popBackStack()
        }
    })



    MessagingScreenContent(
        uiState = uiState,
        state = state.copy(messagesModel = fakeMessages),
        event = { event ->
            when (event) {
                is MessagingUiEvent.ToggleMessageArea -> onToggleExpand()
                is MessagingUiEvent.ResetSelectedIndex -> resetSelectedIndex()
                is MessagingUiEvent.SelectChatItem -> onClickChatItem(event.index)
                is MessagingUiEvent.OnChangeMessage -> onChangeValueMessage(event.message)
                is MessagingUiEvent.OnSelectedConversation -> onSelectedConversation(event.messageItem)
                is MessagingUiEvent.DismissConversationDialog -> onDismissConversationDialog()
                is MessagingUiEvent.OnSendMessage -> onSendMessage()
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}