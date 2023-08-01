package com.example.cyclistance.feature_messaging.presentation.conversation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.cyclistance.feature_messaging.presentation.conversation.components.ConversationContent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationUiEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationUiState

@Composable
fun ConversationScreen(
    viewModel: ConversationViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues,
    newConversationDetails: (name: String, photoUrl: String) -> Unit
) {


    val conversationState = viewModel.conversationState
    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(ConversationUiState()) }
    var message by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
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
            message = it
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

    val closeConversationMessage = remember {
        {
            navController.popBackStack()
        }
    }

    val onSendMessage = remember {
        {
            viewModel.onEvent(
                event = ConversationVmEvent.SendMessage(
                    sendMessageModel = SendMessageModel(
                        receiverId = state.conversationUid,
                        message = message.text
                    )
                )).also {
                message = TextFieldValue()
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

    LaunchedEffect(key1 = state.conversationName, key2 = state.conversationPhotoUrl) {
        newConversationDetails(
             state.conversationName,
             state.conversationPhotoUrl
        )
    }


    ConversationContent(
        conversation = conversationState,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = paddingValues),
        uiState = uiState,
        state = state,
        message = message,
        event = { event ->
            when (event) {
                ConversationUiEvent.CloseConversationScreen -> closeConversationMessage()
                ConversationUiEvent.OnSendMessage -> onSendMessage()
                ConversationUiEvent.ResetSelectedIndex -> resetSelectedIndex()
                is ConversationUiEvent.SelectChatItem -> onClickChatItem(event.index)
                ConversationUiEvent.ToggleMessageArea -> onToggleExpand()
                is ConversationUiEvent.OnChangeValueMessage -> onChangeValueMessage(event.message)
            }
        }
    )
}

