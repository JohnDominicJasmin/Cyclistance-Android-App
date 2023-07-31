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
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.presentation.chat.chats.components.ChatScreenContent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.components.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatUiEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun ChatsScreen(
    viewModel: ChatsViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val onSelectConversation = remember {
        { chatItem: ChatItemModel ->
            val encodedUrl =
                URLEncoder.encode(chatItem.conversionPhoto, StandardCharsets.UTF_8.toString())
            navController.navigateScreen(
                route = "${Screens.MessagingNavigation.ConversationScreen.screenRoute}/${chatItem.conversionId}/$encodedUrl/${chatItem.conversionName}",
            )
        }
    }


    ChatScreenContent(
        state = state.copy(chatsModel = fakeMessages),
        modifier = Modifier.padding(paddingValues),
        event = { event ->
            when (event) {
                is ChatUiEvent.OnSelectConversation -> onSelectConversation(event.chatItem)
            }
        }
    )



}