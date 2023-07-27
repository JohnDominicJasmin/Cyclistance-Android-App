package com.example.cyclistance.feature_messaging.presentation.chat.chats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase
) : ViewModel() {
    private val _state =
        MutableStateFlow(savedStateHandle[MESSAGING_VM_STATE_KEY] ?: ChatState())
    val state = _state.asStateFlow()


    init {
        refreshToken()
    }

    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }


    private fun refreshToken() {
        viewModelScope.launch(SupervisorJob()) {
            runCatching {
                isLoading(true)
                messagingUseCase.refreshTokenUseCase()
            }.onSuccess {
                Timber.v("Successfully refreshed token")
            }.onFailure {
                Timber.e("Failed to refresh token ${it.message}")
            }
            isLoading(false)
        }
    }
}