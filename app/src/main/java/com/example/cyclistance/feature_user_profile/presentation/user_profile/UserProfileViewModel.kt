package com.example.cyclistance.feature_user_profile.presentation.user_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.UserProfileConstants.USER_PROFILE_VM_STATE_KEY
import com.example.cyclistance.feature_user_profile.domain.use_case.UserProfileUseCase
import com.example.cyclistance.feature_user_profile.presentation.user_profile.event.UserProfileVmEvent
import com.example.cyclistance.feature_user_profile.presentation.user_profile.state.UserProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
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
):ViewModel() {

    private val _state = MutableStateFlow(savedStateHandle[USER_PROFILE_VM_STATE_KEY] ?: UserProfileState())
    val state = _state.asStateFlow()

    fun onEvent(event: UserProfileVmEvent){
        when(event){
            is UserProfileVmEvent.LoadProfile -> loadUserProfileInfo(event.userId)
        }

        savedStateHandle[USER_PROFILE_VM_STATE_KEY] = state.value
    }

    private fun loadUserProfileInfo(userId: String) {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                isLoading(true)
                userProfileUseCase.getUserProfileInfoUseCase(id = userId)
            }.onSuccess { profile ->
                _state.update { it.copy(userProfileModel = profile) }
            }.onFailure {
                Timber.v("Error ${it.message}")
            }.also {
                isLoading(false)
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }

}