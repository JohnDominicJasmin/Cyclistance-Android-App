package com.example.cyclistance.feature_messaging.presentation.chat.chats

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants
import com.example.cyclistance.core.utils.constants.MessagingConstants.MESSAGING_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.ChatItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.chat.chats.event.ChatVmEvent
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
    private val messagingUseCase: MessagingUseCase) : ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[MESSAGING_VM_STATE_KEY] ?: ChatState())
    val state = _state.asStateFlow()

    private val _chatsState = mutableStateListOf<ChatItemModel>()
    val chatsState = _chatsState

    init {
        refreshToken()
        initializeListener()
        saveState()
    }



    fun onEvent(event: ChatVmEvent){
        when(event){
            is ChatVmEvent.RefreshChat -> {
                removeChatListener()
                removeUserListener()
                initializeListener()
            }
        }
    }


    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

    private fun initializeListener(){
        addUserListener()
    }

    private fun addUserListener() {
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                messagingUseCase.addUserListenerUseCase()
            }.onSuccess { model ->
                addChatListener(messagingModel = model)
            }.onFailure {
                Timber.e("Failed to retrieve chats ${it.message}")
            }
            isLoading(false)
            saveState()
        }
    }

    private fun saveState(){
        savedStateHandle[MessagingConstants.SEARCH_USER_VM_STATE_KEY] = _state.value
    }

    private fun addChatListener(messagingModel: MessagingUserModel) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.addChatListenerUseCase(onAddedChat = { chat ->

                    val messageUser = messagingModel.users.find { it.userDetails.uid == chat.conversionId }

                    messageUser?.let {
                        val name = it.userDetails.name
                        val photo = it.userDetails.photo
                        chatsState.add(chat.copy(conversionName = name, conversionPhoto = photo))
                    }

                }, onModifiedChat = { chat ->

                    val modifiedIndex = chatsState.indexOfFirst { chat.senderId == it.senderId && chat.receiverId == it.receiverId }
                    val hasFound = modifiedIndex != -1
                    if (hasFound) {
                        chatsState[modifiedIndex] = chatsState[modifiedIndex].copy(
                            lastMessage = chat.lastMessage,
                            timeStamp = chat.timeStamp
                        )
                        chatsState.swap(modifiedIndex, 0)
                    }
                })

            }.onFailure {
                Timber.e("Failed to retrieve chats ${it.message}")
            }
            saveState()
        }
    }

    private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
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
            isLoading(false)
        }
        saveState()
    }

    override fun onCleared() {
        super.onCleared()
        removeChatListener()
        removeUserListener()
    }
}