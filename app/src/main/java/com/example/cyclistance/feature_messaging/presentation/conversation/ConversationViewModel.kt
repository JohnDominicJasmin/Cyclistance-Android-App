package com.example.cyclistance.feature_messaging.presentation.conversation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_VM_STATE_KEY
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_LAST_MESSAGE
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_RECEIVER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_SENDER_ID
import com.example.cyclistance.core.utils.constants.MessagingConstants.KEY_TIMESTAMP
import com.example.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.example.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.example.cyclistance.feature_messaging.domain.model.ui.chats.MessagingUserItemModel
import com.example.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.example.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.example.cyclistance.feature_settings.domain.use_case.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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


    private val _conversationState = mutableStateListOf<ConversationItemModel>()
    val conversationState: List<ConversationItemModel> = _conversationState

    private val _state = MutableStateFlow(
        savedStateHandle[CONVERSATION_VM_STATE_KEY] ?: ConversationState())

    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ConversationEvent>()
    val event = _event.asSharedFlow()


    private fun initialize(
        userReceiverMessage: MessagingUserItemModel,
        userSenderMessage: MessagingUserItemModel) {

        val isInitialized = state.value.userReceiverMessage != null
        if (isInitialized) {
            return
        }

        _state.update {
            it.copy(
                userReceiverMessage = userReceiverMessage,
                userSenderMessage = userSenderMessage)
        }

        val receiverId = userReceiverMessage.userDetails.uid
        addMessageListener(receiverId = receiverId)
        getConversionId(receiverId = receiverId)
        getUid()
        getName()
        getPhoto()
        saveState()
    }

    fun onEvent(event: ConversationVmEvent) {
        when (event) {
            is ConversationVmEvent.SendMessage -> sendMessage(event.sendMessageModel)
            is ConversationVmEvent.OnInitialized -> initialize(
                userReceiverMessage = event.userReceiverMessage,
                userSenderMessage = event.userSenderMessage)
        }
        saveState()
    }


    private fun saveState() {
        savedStateHandle[CONVERSATION_VM_STATE_KEY] = state.value
    }


    private fun getName() {
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

    private fun getPhoto() {
        viewModelScope.launch {
            runCatching {
                settingUseCase.getPhotoUrlUseCase()
            }.onSuccess { photo ->
                _state.update { it.copy(userPhoto = photo) }
            }.onFailure {
                Timber.v("Failed to get photo: ${it.message}")
            }
        }
    }

    private fun setConversion(message: String) {
        if (state.value.conversionId == null) {
            addConversion(message = message)
            return
        }
        updateConversion(message = message)
    }

    private fun updateConversion(message: String) {
        val conversionId = state.value.conversionId
        viewModelScope.launch {
            runCatching {
                messagingUseCase.updateConversionUseCase(
                    message = message,
                    conversionId = conversionId!!)
            }.onSuccess {
                Timber.v("Success to update conversion")
            }.onFailure {
                Timber.e("Failed to update conversion: ${it.message}")
            }
        }
    }

    private fun addConversion(message: String) {
        runCatching {
            messagingUseCase.addConversionUseCase(
                conversion = state.handleConversion(message),
                onNewConversionId = { id ->
                    _state.update { it.copy(conversionId = id) }
                })
        }.onSuccess {
            Timber.v("Success to add conversion")
        }.onFailure {
            Timber.e("Failed to add conversion: ${it.message}")
        }
    }

    private fun StateFlow<ConversationState>.handleConversion(message: String): HashMap<String, Any> {
        return with(value) {
            hashMapOf(
                KEY_SENDER_ID to userUid,
                KEY_RECEIVER_ID to userReceiverMessage?.userDetails?.uid!!,
                KEY_LAST_MESSAGE to message,
                KEY_TIMESTAMP to Date()
            )
        }
    }

    private fun getConversionId(receiverId: String) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.getConversionIdUseCase(receiverId = receiverId)
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

    private fun sendMessage(model: SendMessageModel) {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.sendMessageUseCase(model)
                sendMessageNotification(model.message)
            }.onSuccess {
                _event.emit(ConversationEvent.MessageSent)
                setConversion(model.message)
            }.onFailure {
                _event.emit(ConversationEvent.MessageNotSent)
            }
        }
    }

    private fun sendMessageNotification(message: String){
        val userReceiver = state.value.userReceiverMessage
        val messageUser = state.value.userSenderMessage!!
        if(userReceiver!!.isUserAvailable){
            return
        }
        viewModelScope.launch {
            runCatching {
                messagingUseCase.sendNotificationUseCase(
                    SendNotificationModel(
                        userReceiverMessage = userReceiver,
                        userSenderMessage = messageUser,
                        senderName = state.value.userName,
                        message = message
                    ))
            }.onSuccess {
                Timber.v("Successfully send message notification")
            }.onFailure {
                Timber.e("Failed to send message notification")
            }
        }
    }

    private fun removeMessageListener() {
        messagingUseCase.removeMessageListenerUseCase()
    }

    private fun isLoading(isLoading: Boolean) {
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