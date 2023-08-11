package com.example.cyclistance.feature_authentication.presentation.auth_reset_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.AuthConstants.RESET_PASSWORD_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordEvent
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.event.ResetPasswordVmEvent
import com.example.cyclistance.feature_authentication.presentation.auth_reset_password.state.ResetPasswordState
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
class ResetPasswordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
) : ViewModel() {


    private val _state =
        MutableStateFlow(savedStateHandle[RESET_PASSWORD_VM_STATE_KEY] ?: ResetPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ResetPasswordEvent>()
    val event = _event.asSharedFlow()


    private fun isLoading(loading: Boolean){
        _state.update { it.copy(isLoading = loading) }
    }

    private fun resetPassword(currentPassword: String, newPassword: String, confirmPassword: String){
        viewModelScope.launch {
            runCatching {
                isLoading(true)
                authUseCase.changePasswordUseCase(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    confirmPassword = confirmPassword
                )
            }.onSuccess {
                _eventFlow.emit(value = ResetPasswordEvent.ResetPasswordSuccess)
            }.onFailure {
                it.handleResetPasswordException()
                Timber.v("ResetPasswordViewModel: resetPassword: onFailure: ${it.message}")
            }.also {
                isLoading(false)
            }
        }
    }

    private suspend fun Throwable.handleResetPasswordException(){
        when (this) {
            is AuthExceptions.CurrentPasswordException -> {
                _eventFlow.emit(value = ResetPasswordEvent.CurrentPasswordFailed(message.toString()))
            }

            is AuthExceptions.NewPasswordException -> {
                _eventFlow.emit(value = ResetPasswordEvent.NewPasswordFailed(message.toString()))
            }

            is AuthExceptions.ConfirmPasswordException -> {
                _eventFlow.emit(value = ResetPasswordEvent.ConfirmPasswordFailed(message.toString()))
            }
        }
    }

    fun onEvent(event: ResetPasswordVmEvent) {
        when (event) {

            is ResetPasswordVmEvent.ResetPassword -> resetPassword(
                currentPassword = event.currentPassword,
                newPassword = event.newPassword,
                confirmPassword = event.confirmPassword
            )
        }
        savedStateHandle[RESET_PASSWORD_VM_STATE_KEY] = state.value
    }


}