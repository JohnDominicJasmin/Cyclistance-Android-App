package com.myapp.cyclistance.feature_messaging.presentation.conversation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.list.ListUtils
import com.myapp.cyclistance.core.utils.constants.MessagingConstants.CONVERSATION_VM_STATE_KEY
import com.myapp.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.myapp.cyclistance.feature_messaging.domain.model.SendMessageModel
import com.myapp.cyclistance.feature_messaging.domain.model.SendNotificationModel
import com.myapp.cyclistance.feature_messaging.domain.model.ui.conversation.ConversationItemModel
import com.myapp.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.myapp.cyclistance.feature_messaging.presentation.conversation.event.ConversationEvent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.event.ConversationVmEvent
import com.myapp.cyclistance.feature_messaging.presentation.conversation.state.ConversationState
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
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
    private val messagingUseCase: MessagingUseCase,
    private val userProfileUseCase: UserProfileUseCase,
    private val authUseCase: AuthenticationUseCase
) : ViewModel() {


    private val _conversationState = mutableStateListOf<ConversationItemModel>()
    val conversationState: List<ConversationItemModel> = _conversationState

    private val _state = MutableStateFlow(
        savedStateHandle[CONVERSATION_VM_STATE_KEY] ?: ConversationState())

    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ConversationEvent>()
    val event = _eventFlow.asSharedFlow()


    private fun initialize(
        userReceiverId: String,
    ) {
        loadConversationSelected(receiverId = userReceiverId)
        addMessageListener(receiverId = userReceiverId)
        getConversionId(receiverId = userReceiverId)
        getUid()
        getName()
        saveState()
    }

    private fun getId(): String = authUseCase.getIdUseCase()

    private fun loadConversationSelected(receiverId: String) {
        viewModelScope.launch {
            runCatching {

                val sender = async {
                    if (state.value.userSenderMessage == null) {
                        messagingUseCase.getMessagingUserUseCase(uid = getId())
                    } else {
                        state.value.userSenderMessage
                    }
                }.await()

                val receiver = async {
                    if (state.value.userReceiverMessage?.userDetails?.uid != receiverId) {
                        messagingUseCase.getMessagingUserUseCase(uid = receiverId)
                    } else {
                        state.value.userReceiverMessage
                    }
                }.await()

                _state.update {
                    it.copy(
                        userSenderMessage = sender,
                        userReceiverMessage = receiver
                    )
                }
            }.onSuccess {
                Timber.v("Messaging User Success")
            }.onFailure {
                Timber.e("Messaging User Error: ${it.localizedMessage}")
            }
        }
    }

    fun onEvent(event: ConversationVmEvent) {
        when (event) {
            is ConversationVmEvent.SendMessage -> sendMessage(event.sendMessageModel)
            is ConversationVmEvent.OnInitialized -> initialize(userReceiverId = event.userReceiverId)
            ConversationVmEvent.ResendMessage -> resendMessage()
            is ConversationVmEvent.MarkAsSeen -> markAsSeen(event.messageId)
        }
        saveState()
    }


    private fun markAsSeen(messageId: String) {
        viewModelScope.launch {
            state.value.conversionId?.let { conversionId ->
                messagingUseCase.markAsSeenUseCase(
                    messageId = messageId,
                    conversionId = conversionId)
            }
        }
    }

    private fun saveState() {
        savedStateHandle[CONVERSATION_VM_STATE_KEY] = state.value
    }

    private fun resendMessage() {
        viewModelScope.launch {
            runCatching {
                messagingUseCase.reEnableNetworkSyncUseCase()
            }.onSuccess {
                Timber.v("Success to re-enable network sync")
            }.onFailure {
                _eventFlow.emit(value = ConversationEvent.ResendMessageFailed(it.message!!))
            }
        }
    }

    private fun getName() {
        viewModelScope.launch {
            runCatching {
                userProfileUseCase.getNameUseCase()
            }.onSuccess { name ->
                _state.update { it.copy(userName = name) }
            }.onFailure {
                Timber.v("Failed to get name: ${it.message}")
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
        val receiverId = state.value.getReceiverId()
        viewModelScope.launch {
            runCatching {
                messagingUseCase.updateConversionUseCase(
                    message = message,
                    receiverId = receiverId,
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
            val receiverId = state.value.getReceiverId()
            messagingUseCase.addConversionUseCase(
                receiverId = receiverId,
                message = message,
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
        viewModelScope.launch(SupervisorJob()) {
            setConversion(model.message)
            messagingUseCase.sendMessageUseCase(model)
            sendMessageNotification(model.message)
        }
    }

    private fun sendMessageNotification(message: String) {
        val userReceiver = state.value
        viewModelScope.launch {
            runCatching {
                messagingUseCase.sendNotificationUseCase(
                    SendNotificationModel(
                        conversationId = state.value.userSenderMessage!!.getUid(),
                        senderName = state.value.userName,
                        message = message,
                        userReceiverToken = userReceiver.getReceiverToken()
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
                        _conversationState.updateMessages(conversation.messages)
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

    private fun MutableList<ConversationItemModel>.updateMessages(apiMessage: List<ConversationItemModel>) {
        val notEqualIndex = zip(apiMessage).indexOfLast { (n1, n2) -> n1.isSent != n2.isSent }

        if (ListUtils.isEqual(first = this, second = apiMessage)) {
            return
        }

        if (notEqualIndex == -1) {
            clear()
            addAll(apiMessage)
            return
        }
        set(notEqualIndex, element = apiMessage[notEqualIndex])
    }


    override fun onCleared() {
        super.onCleared()
        removeMessageListener()
        _conversationState.clear()
    }
}