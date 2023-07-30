package com.example.cyclistance.feature_messaging.presentation.conversation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.CHAT_PHOTO_URL
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_LAST_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_IMAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_IMAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_NAME
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_TIMESTAMP
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val messagingUseCase: MessagingUseCase,
    private val settingUseCase: SettingUseCase
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
        getConversionId()
        getUid()
        getName()
        getPhoto()
    }

    fun onEvent(event: ConversationVmEvent) {
        when (event) {
            is ConversationVmEvent.SendMessage -> sendMessage(event.sendMessageModel)
        }
    }


    private fun getName(){
        viewModelScope.launch {
            runCatching {
                settingUseCase.getNameUseCase()
            }.onSuccess { name ->
                _state.update { it.copy(userName = name) }
            }.onFailure {
                Timber.v("Failed to get name: ${it.message}")
            }
        }
    }

    private fun getPhoto(){
        viewModelScope.launch {
            runCatching {
                settingUseCase.getPhotoUrlUseCase()
            }.onSuccess { photo ->
                _state.update { it.copy(userPhoto = photo ) }
            }.onFailure {
                Timber.v("Failed to get photo: ${it.message}")
            }
        }
    }

    private fun setConversion(message: String){
        if(state.value.conversionId == null){
            addConversion(message = message)
            return
        }
        updateConversion(message = message)
    }

    private fun updateConversion(message: String){
        val conversionId = state.value.conversionId
        viewModelScope.launch {
            runCatching {
                messagingUseCase.updateConversionUseCase(message = message, conversionId = conversionId!!)
            }.onSuccess {
                Timber.v("Success to update conversion")
            }.onFailure {
                Timber.e("Failed to update conversion: ${it.message}")
            }
        }
    }

    private fun addConversion(message: String){

        runCatching {
            messagingUseCase.addConversionUseCase(
                conversion = hashMapOf(
                    KEY_SENDER_ID to state.value.userUid,
                    KEY_SENDER_NAME to state.value.userName,
                    KEY_SENDER_IMAGE to state.value.userPhoto,
                    KEY_RECEIVER_ID to _chatId,
                    KEY_RECEIVER_NAME to _chatName,
                    KEY_RECEIVER_IMAGE to _chatPhotoUrl,
                    KEY_LAST_MESSAGE to message,
                    KEY_TIMESTAMP to Date()
                ),
                onNewConversionId = { id ->
                    _state.update { it.copy(conversionId = id) }
                }
            )
        }.onSuccess {
            Timber.v("Success to add conversion")
        }.onFailure {
            Timber.e("Failed to add conversion: ${it.message}")
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
                setConversion(sendMessage.message)
            }.onFailure {
                _event.emit(ConversationEvent.MessageNotSent)
            }
        }
    }

    private fun removeMessageListener() {
        messagingUseCase.removeMessageListenerUseCase()
    }

    private fun isLoading(isLoading: Boolean){
        _state.update { it.copy(isLoading = isLoading) }
    }
    private fun addMessageListener(receiverId: String) {
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                messagingUseCase.addMessageListenerUseCase(
                    receiverId = receiverId,
                    onNewMessage = { conversation ->
                        _conversationState.addAll(conversation.messages)
                        isLoading(false)
                    })
            }.onSuccess {
                Timber.v("Success to add message listener")
            }.onFailure {
                isLoading(false)
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