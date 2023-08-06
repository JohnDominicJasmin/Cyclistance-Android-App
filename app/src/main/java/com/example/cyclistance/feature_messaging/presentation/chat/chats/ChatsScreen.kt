package com.example.cyclistance.feature_messaging.presentation.chat.chats

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.components.ChatScreenContent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatUiEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatVmEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues,
    isInternetAvailable: Boolean) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val chatState = viewModel.chatsState.distinctBy { it.second.conversionId }


    val onSelectConversation = remember {
        { user: MessagingUserItemModel ->
            val encodedUrl =
                URLEncoder.encode(user.userDetails.photo, StandardCharsets.UTF_8.toString())
            val jsonString = Gson().toJson(user.copy(userDetails = user.userDetails.copy(photo = encodedUrl)))

            navController.navigateScreen(
                route = "${Screens.MessagingNavigation.ConversationScreen.screenRoute}/${jsonString}",
            )
        }
    }

    val onRefreshChats = remember{{
        viewModel.onEvent(event = ChatVmEvent.RefreshChat)
    }}


    ChatScreenContent(
        chatState = chatState,
        state = state,
        modifier = Modifier.padding(paddingValues),
        isInternetAvailable = isInternetAvailable,
        event = { event ->
            when (event) {
                is ChatUiEvent.OnSelectConversation -> onSelectConversation(event.user)
                is ChatUiEvent.OnRefreshChat -> onRefreshChats()
            }
        }
    )



}