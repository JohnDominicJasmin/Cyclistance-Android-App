package com.myapp.cyclistance.feature_user_profile.presentation.user_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.cyclistance.core.utils.constants.UserProfileConstants.USER_PROFILE_VM_STATE_KEY
import com.myapp.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.myapp.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileEvent
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileVmEvent
import com.myapp.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
) : ViewModel() {

    private val _state =
        MutableStateFlow(savedStateHandle[USER_PROFILE_VM_STATE_KEY] ?: UserProfileState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UserProfileEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    init {
        loadUserId()
    }



    fun onEvent(event: UserProfileVmEvent) {

        when (event) {
            is UserProfileVmEvent.LoadProfile -> loadUserProfileInfo(event.userId)

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