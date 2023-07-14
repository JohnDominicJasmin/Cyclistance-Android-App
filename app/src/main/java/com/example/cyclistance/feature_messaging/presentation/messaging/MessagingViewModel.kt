package com.example.cyclistance.feature_messaging.presentation.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessagingViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _state =
        MutableStateFlow(savedStateHandle[MESSAGING_VM_STATE_KEY] ?: MessagingState())
    val state = _state.asStateFlow()
}