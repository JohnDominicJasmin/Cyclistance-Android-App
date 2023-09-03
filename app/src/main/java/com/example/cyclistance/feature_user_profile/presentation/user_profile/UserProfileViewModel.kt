package com.example.cyclistance.feature_user_profile.presentation.user_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_messaging.domain.use_case.MessagingUseCase
import com.example.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileEvent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileVmEvent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
class UserProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val defaultDispatcher: CoroutineDispatcher,
    private val userProfileUseCase: UserProfileUseCase,
    private val authUseCase: AuthenticationUseCase,
    private val messagingUseCase: MessagingUseCase
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[USER_PROFILE_VM_STATE_KEY] ?: UserProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UserProfileEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    init {
        loadUserId()
    }

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
                    if(state.value.userReceiverMessage?.userDetails?.uid != receiverId){
                        messagingUseCase.getMessagingUserUseCase(uid = receiverId)
                    }else{
                        state.value.userReceiverMessage
                    }
                }.await()

                _state.update {
                    it.copy(
                        userSenderMessage = sender,
                        userReceiverMessage = receiver
                    )
                }
                _eventFlow.emit(value = UserProfileEvent.LoadConversationSuccess(
                    userSenderMessage = sender!!,
                    userReceiverMessage = receiver!!
                ))

            }.onSuccess {
                Timber.v("Messaging User Success")
            }.onFailure {
                Timber.e("Messaging User Error: ${it.localizedMessage}")
            }
        }
    }


    fun onEvent(event: UserProfileVmEvent) {

        when (event) {
            is UserProfileVmEvent.LoadProfile -> loadUserProfileInfo(event.userId)
            is UserProfileVmEvent.LoadConversationSelected -> loadConversationSelected(receiverId = event.userId)
        }

        savedStateHandle[USER_PROFILE_VM_STATE_KEY] = state.value
    }


    private fun loadUserId() {
        runCatching {
            getId()
        }.onSuccess { id ->
            _state.update { it.copy(userId = id) }
        }.onFailure {
            Timber.e("Load user id ${it.message}")
        }
    }

    private fun loadUserProfileInfo(userId: String) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                isLoading(true)
                userProfileUseCase.getUserProfileInfoUseCase(id = userId)
            }.onSuccess { profile ->
                _state.update { it.copy(userProfileModel = profile, profileSelectedId = userId) }
            }.onFailure {
                Timber.v("Error ${it.message}")
            }.also {
                isLoading(false)
            }
        }
    }

    private fun getId() = authUseCase.getIdUseCase()

    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

}