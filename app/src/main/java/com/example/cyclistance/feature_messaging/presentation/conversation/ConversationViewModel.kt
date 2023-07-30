package com.example.cyclistance.feature_messaging.presentation.conversation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_PHOTO_URL
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_VM_STATE_KEY
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase
) : ViewModel() {


    private val _chatName: String = savedStateHandle[CHAT_NAME]!!
    private val _chatId: String = savedStateHandle[CHAT_ID]!!
    private val _chatPhotoUrl: String = savedStateHandle[CHAT_PHOTO_URL]!!

    private val _conversationState = mutableStateListOf<ConversationItemModel>()
    val conversationState: List<ConversationItemModel> = _conversationState

    private val _state = MutableStateFlow(
        savedStateHandle[CONVERSATION_VM_STATE_KEY] ?: ConversationState(
            conversationUid = _chatId,
            conversationPhotoUrl = _chatPhotoUrl,
            conversationName = _chatName
        ))

    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ConversationEvent>()
    val event = _event.asSharedFlow()


    init {
        addMessageListener(_chatId)
        getUid()
        getConversionId()
    }

    fun onEvent(event: ConversationVmEvent) {
        when (event) {
            is ConversationVmEvent.SendMessage -> sendMessage(event.sendMessageModel)
        }
    }

    private fun getConversionId(){
        viewModelScope.launch {
            runCatching {
                messagingUseCase.getConversionIdUseCase(receiverId = _chatId)
            }.onSuccess { id ->
                _state.update { it.copy(conversionId = id) }
            }.onFailure {
                Timber.e("Failed to get conversation id: ${it.message}")
            }
        }
    }

    private fun getUid() {
        runCatching {
            messagingUseCase.getUidUseCase()
        }.onSuccess { id ->
            _state.update { it.copy(userUid = id) }
        }.onFailure {
            Timber.e("Failed to get uid: ${it.message}")
        }
    }

    private fun sendMessage(sendMessage: SendMessageModel) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.sendMessageUseCase(sendMessage)
            }.onSuccess {
                _event.emit(ConversationEvent.MessageSent)
            }.onFailure {
                _event.emit(ConversationEvent.MessageNotSent)
            }
        }
    }

    private fun removeMessageListener() {
        messagingUseCase.removeMessageListenerUseCase()
    }

    private fun addMessageListener(receiverId: String) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.addMessageListenerUseCase(
                    receiverId = receiverId,
                    onNewMessage = { conversation ->
                        _conversationState.addAll(conversation.messages)
                    })
            }.onSuccess {

            }.onFailure {
                Timber.e("Failed to add message listener ${it.message}")
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        removeMessageListener()
        _conversationState.clear()
    }
}