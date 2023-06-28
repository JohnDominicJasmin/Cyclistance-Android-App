package com.example.cyclistance.feature_message.presentation.messaging_conversation

import androidx.lifecycle.ViewModel
import com.example.cyclistance.feature_message.presentation.messaging_conversation.state.MessagingConversationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessagingConversationViewModel : ViewModel() {
    private val _state = MutableStateFlow(MessagingConversationState())
    val state = _state.asStateFlow()
}