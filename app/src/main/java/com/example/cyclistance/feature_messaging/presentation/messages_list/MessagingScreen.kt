package com.example.cyclistance.feature_messaging.presentation.messages_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_messaging.presentation.messages_list.components.MessagingScreenContent
import com.example.cyclistance.feature_messaging.presentation.messages_list.components.fakeMessages
import com.example.cyclistance.feature_messaging.presentation.messages_list.event.MessageUiEvent
import com.example.cyclistance.navigation.Screens

@Composable
fun MessagingScreen(navController: NavController, paddingValues: PaddingValues) {

    MessagingScreenContent(
        messagesModel = fakeMessages,
        event = {
            when (it) {
                is MessageUiEvent.OnMessageClicked -> {
                    navController.navigate(Screens.Messaging.ConversationScreen.screenRoute)
                }
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}