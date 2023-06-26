package com.example.cyclistance.feature_message.presentation.messaging_main_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_message.presentation.messaging_main_screen.components.MessagingScreenContent
import com.example.cyclistance.feature_message.presentation.messaging_main_screen.components.fakeMessages
import com.example.cyclistance.feature_message.presentation.messaging_main_screen.event.MessageUiEvent
import com.example.cyclistance.navigation.Screens

@Composable
fun MessagingScreen(navController: NavController, paddingValues: PaddingValues) {

    MessagingScreenContent(
        messagesModel = fakeMessages,
        event = {
            when (it) {
                is MessageUiEvent.OnMessageClicked -> {
                    navController.navigate(Screens.MessagingConversationScreen.route)
                }
            }
        },
        modifier = Modifier.padding(paddingValues)
    )
}