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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list.MessagingScreenContent
import com.example.cyclistance.feature_messaging.presentation.messaging.components.messaging_list.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.messaging.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingUiState

@Composable
fun MessagingScreen(
    viewModel: MessagingViewModel = viewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    var uiState by rememberSaveable { mutableStateOf(MessagingUiState()) }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val onSelectedConversationId = remember {
        { id: String ->
            uiState = uiState.copy(
                selectedConversationId = id
            )
        }
    }

    val onDismissConversationDialog = remember {
        {
            uiState = uiState.copy(
                selectedConversationId = null
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
                is MessagingUiEvent.OnSelectedConversation -> onSelectedConversationId(event.conversationId)
                is MessagingUiEvent.DismissConversationDialog -> onDismissConversationDialog()
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}