package com.example.cyclistance.feature_messaging.presentation.chat.chats

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.model.helper.MessagingUserHandler
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatVmEvent
import com.example.cyclistance.feature_messaging.presentation.chat.chats.state.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[MESSAGING_VM_STATE_KEY] ?: ChatState())
    val state = _state.asStateFlow()

    private val messageUserFlow = MutableStateFlow(MessagingUserModel())


    private val _chatsState = mutableStateListOf<ChatItemModel>()
    val chatsState: List<ChatItemModel> = _chatsState

    init {
        refreshToken()
        initializeListener()
        saveState()
    }


    fun onEvent(event: ChatVmEvent) {
        when (event) {
            is ChatVmEvent.RefreshChat -> {
                _chatsState.clear()
                removeChatListener()
                removeUserListener()
                initializeListener()
            }
        }
    }


    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun initializeListener() {
        viewModelScope.launch(SupervisorJob()) {
            addUserListener()
            addChatListener()
        }

    }

    private suspend fun addUserListener() {

        coroutineScope {
            runCatching {
                messagingUseCase.addUserListenerUseCase(onNewMessageUser = { messageUser ->
                    this.launch(SupervisorJob()) {
                        messageUserFlow.emit(value = messageUser)
                    }
                })
            }.onFailure {
                Timber.e("Failed to retrieve chats ${it.message}")
            }
            saveState()
        }
    }

    private fun saveState() {
        savedStateHandle[MessagingConstants.SEARCH_USER_VM_STATE_KEY] = _state.value
    }

    private suspend fun addChatListener() {
        coroutineScope {
            runCatching {
                messagingUseCase.addChatListenerUseCase(onAddedChat = { chat ->
                    this.launch(SupervisorJob()) {
                        handleAddChat(chat)
                    }
                }, onModifiedChat = { chat ->
                    handleModifiedChat(chat)
                })

            }.onFailure {
                Timber.e("Failed to retrieve chats ${it.message}")
            }.also {
                isLoading(false)
                saveState()
            }
        }
    }


    private suspend fun handleAddChat(chat: ChatItemModel){

            messageUserFlow.collect { messageUser ->
                val matchedMessageUser =
                    messageUser.users.find { it.userDetails.uid == chat.conversionId }
                val messagingUserHandler = matchedMessageUser?.let {
                    MessagingUserHandler(
                        messagingUserItem = it,
                        chatItem = chat,
                        chats = _chatsState
                    )
                }
                messagingUserHandler?.handleNewAddedChat()
                _chatsState.addAll(messagingUserHandler?.chats ?: emptyList())
                isLoading(false)
            }

        }


    private fun handleModifiedChat(chat: ChatItemModel){
        val messagingUserHandler = MessagingUserHandler(
            chatItem = chat,
            chats = _chatsState
        )
        messagingUserHandler.handleModifiedChat()
        _chatsState.addAll(messagingUserHandler.chats)
        isLoading(false)
    }


    private fun removeChatListener() {
        messagingUseCase.removeChatListenerUseCase()
    }

    private fun removeUserListener() {
        messagingUseCase.removeUserListenerUseCase()
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

        }
        saveState()
    }

    override fun onCleared() {
        super.onCleared()
        removeChatListener()
        removeUserListener()
    }
}


