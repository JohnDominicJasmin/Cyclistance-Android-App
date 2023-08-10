package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.constants.AuthConstants.FORGOT_PASSWORD_VM_STATE_KEY
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordVmEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow( savedStateHandle[FORGOT_PASSWORD_VM_STATE_KEY] ?: ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<ForgotPasswordEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: ForgotPasswordVmEvent) {
        when (event) {
            is ForgotPasswordVmEvent.SendPasswordResetEmail -> sendPasswordResetEmail(email = event.email)
        }
        savedStateHandle[FORGOT_PASSWORD_VM_STATE_KEY] = state.value
    }

    private fun isLoading(loading: Boolean) {
        _state.update { it.copy(isLoading = loading) }
    }

    private fun sendPasswordResetEmail(email: String) {
        viewModelScope.launch {
            isLoading(true)
            runCatching {
                authUseCase.sendPasswordResetEmailUseCase(email = email)
            }.onSuccess {
                _eventFlow.emit(value = ForgotPasswordEvent.ForgotPasswordSuccess)
            }.onFailure { exception ->
                exception.handleSendPasswordResetEmail()
            }
        }
        isLoading(false)
    }


    private suspend fun Throwable.handleSendPasswordResetEmail() {
        when (this) {
            is AuthExceptions.NetworkException -> {
                _eventFlow.emit(value = ForgotPasswordEvent.NoInternetConnection)
            }

            is AuthExceptions.EmailException -> {
                _eventFlow.emit(
                    value = ForgotPasswordEvent.InvalidEmail(
                        reason = this.message ?: "Invalid email"))
            }

            else -> {
                _eventFlow.emit(value = ForgotPasswordEvent.ForgotPasswordFailed(
                    reason = this.message ?: "Sending password reset email failed"))
            }
        }
    }
}