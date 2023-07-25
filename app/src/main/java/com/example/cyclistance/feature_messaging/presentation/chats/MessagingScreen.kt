package com.example.cyclistance.feature_messaging.presentation.chats

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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel
import com.example.cyclistance.feature_messaging.presentation.chats.components.MessagingScreenContent
import com.example.cyclistance.feature_messaging.presentation.chats.components.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.chats.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.chats.state.MessagingUiState
import com.example.cyclistance.navigation.Screens
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MessagingScreen(
    viewModel: MessagingViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    val focusRequester = remember { FocusRequester() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(MessagingUiState()) }
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue())
    }
    val closeMessagingScreen = remember {
        {
            navController.popBackStack()
        }
    }
    val onSelectConversation = remember {
        { chatItem: ChatItemModel ->
            val encodedUrl =
                URLEncoder.encode(chatItem.userPhotoUrl, StandardCharsets.UTF_8.toString())
            navController.navigate(
                route = "${Screens.Messaging.ConversationScreen.screenRoute}/${chatItem.userId}/$encodedUrl/${chatItem.name}")
        }
    }
    val onClickSearch = remember {
        {
            uiState = uiState.copy(
                isSearching = true
            )
        }
    }

    val onSearchQueryChanged = remember {
        { _searchQuery: TextFieldValue ->
            searchQuery = _searchQuery
        }
    }

    val onClearSearchQuery = remember {
        {
            searchQuery = TextFieldValue()
        }
    }

    BackHandler {
        if (uiState.isSearching) {
            uiState = uiState.copy(isSearching = false)
        } else {
            closeMessagingScreen()
        }
    }





    MessagingScreenContent(
        state = state.copy(chatsModel = fakeMessages),
        uiState = uiState,
        modifier = Modifier.padding(paddingValues),
        searchQuery = searchQuery,
        focusRequester = focusRequester,
        event = { event ->
            when (event) {
                is MessagingUiEvent.CloseMessagingScreen -> closeMessagingScreen()
                is MessagingUiEvent.OnSelectConversation -> onSelectConversation(event.messageItem)
                is MessagingUiEvent.OnClickSearch -> onClickSearch()
                is MessagingUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
                is MessagingUiEvent.ClearSearchQuery -> onClearSearchQuery()
            }
        },
    )
}