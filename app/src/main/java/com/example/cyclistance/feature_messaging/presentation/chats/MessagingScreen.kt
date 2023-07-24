package com.example.cyclistance.feature_messaging.presentation.chats

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.domain.model.ui.list_messages.ChatItemModel
import com.example.cyclistance.feature_messaging.presentation.chats.components.MessagingScreenContent
import com.example.cyclistance.feature_messaging.presentation.chats.components.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.chats.event.MessagingUiEvent
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MessagingScreen(
    viewModel: MessagingViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    val state by viewModel.state.collectAsStateWithLifecycle()


    val closeMessagingScreen = remember {
        {
            navController.popBackStack()
        }
    }
    val onSelectConversation = remember {
        { chatItem: ChatItemModel ->
            val encodedUrl =
                URLEncoder.encode(chatItem.userPhotoUrl, StandardCharsets.UTF_8.toString())
            navController.navigateScreen(
                destination = "${Screens.Messaging.ConversationScreen.screenRoute}/${chatItem.userId}/$encodedUrl/${chatItem.name}",
                popUpToDestination = Screens.Messaging.MessagingScreen.screenRoute)
        }
    }





    MessagingScreenContent(
        state = state.copy(chatsModel = fakeMessages),
        event = { event ->
            when (event) {
                is MessagingUiEvent.CloseMessagingScreen -> closeMessagingScreen()
                is MessagingUiEvent.OnSelectConversation -> onSelectConversation(event.messageItem)
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}