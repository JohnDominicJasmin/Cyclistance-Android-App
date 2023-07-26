package com.example.cyclistance.feature_messaging.presentation.chat.chats

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
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.components.ChatScreenContent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.MessagingUiEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.MessagingUiState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    val focusRequester = remember { FocusRequester() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    var uiState by rememberSaveable { mutableStateOf(MessagingUiState()) }
    var searchQuery by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(
            TextFieldValue())
    }
    val onDismissSearchMessageDialog = remember {
        {
            uiState = uiState.copy(
                isSearching = false
            )
        }
    }

    val closeMessagingScreen = remember {
        {
            if (uiState.isSearching) {
                onDismissSearchMessageDialog()
            } else {
                navController.popBackStack()
            }
        }
    }
    val onSelectConversation = remember {
        { chatItem: MessagingUserItemModel ->
            val encodedUrl =
                URLEncoder.encode(chatItem.userDetails.photo, StandardCharsets.UTF_8.toString())
            navController.navigateScreen(
                destination = "${Screens.MessagingNavigation.ConversationScreen.screenRoute}/${chatItem.userDetails.uid}/$encodedUrl/${chatItem.userDetails.name}",
                popUpToDestination = Screens.MessagingNavigation.ChatScreen.screenRoute
            )
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
        closeMessagingScreen()
    }





    ChatScreenContent(
        state = state,
        uiState = uiState,
        modifier = Modifier.padding(paddingValues),
        searchQuery = searchQuery,
        focusRequester = focusRequester,
        event = { event ->
            when (event) {
                is MessagingUiEvent.CloseScreen -> closeMessagingScreen()
                is MessagingUiEvent.OnSelectConversation -> onSelectConversation(event.messageItem)
                is MessagingUiEvent.OnClickSearch -> onClickSearch()
                is MessagingUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.searchQuery)
                is MessagingUiEvent.ClearSearchQuery -> onClearSearchQuery()
                is MessagingUiEvent.DismissSearchMessageDialog -> onDismissSearchMessageDialog()
            }
        },
    )
}