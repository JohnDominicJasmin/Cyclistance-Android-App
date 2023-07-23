package com.example.cyclistance.feature_messaging.presentation.messaging

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.messaging.event.MessagingEvent
import com.example.cyclistance.feature_messaging.presentation.messaging.state.MessagingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MessagingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase
) : ViewModel() {
    private val _state =
        MutableStateFlow(savedStateHandle[MESSAGING_VM_STATE_KEY] ?: MessagingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<MessagingEvent>()
    val event = _event.asSharedFlow()

    init {
        refreshToken()
    }


    fun onEvent(event: MessagingEvent) {
        when (event) {
            is MessagingEvent.SendMessage -> sendMessage(event.sendMessageModel)
        }
    }

    private fun sendMessage(sendMessage: SendMessageModel) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.sendMessageUseCase(sendMessage)
            }.onSuccess {
                Timber.v("Successfully sent message")
            }.onFailure {
                Timber.e("Failed to send message ${it.message}")
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch(SupervisorJob()) {
            runCatching {
                messagingUseCase.refreshTokenUseCase()
            }.onSuccess {
                Timber.v("Successfully refreshed token")
            }.onFailure {
                Timber.e("Failed to refresh token ${it.message}")
            }
        }
    }
}