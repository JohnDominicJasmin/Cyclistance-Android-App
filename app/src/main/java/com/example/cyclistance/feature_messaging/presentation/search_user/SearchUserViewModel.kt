package com.example.cyclistance.feature_messaging.presentation.search_user

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.SEARCH_USER_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.search_user.state.SearchUserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase
): ViewModel(){

    private val _state = MutableStateFlow(savedStateHandle[SEARCH_USER_VM_STATE_KEY] ?: SearchUserState())
    val state = _state.asStateFlow()

    init{
        getUsers()
    }

    private fun getUsers() {
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                messagingUseCase.addUserListenerUseCase(onNewMessageUser = { model ->
                    _state.update { it.copy(messagingUsers = model) }
                })
            }.onFailure {
                Timber.e("Failed to retrieve chats ${it.message}")
            }
            isLoading(false)
            savedStateHandle[SEARCH_USER_VM_STATE_KEY] = _state.value
        }
    }
    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    override fun onCleared() {
        super.onCleared()
        messagingUseCase.removeUserListenerUseCase()
    }
}