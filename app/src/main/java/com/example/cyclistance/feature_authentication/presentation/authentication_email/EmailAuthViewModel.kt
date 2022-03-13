package com.example.cyclistance.feature_authentication.presentation.authentication_email

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailAuthViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _reloadState: MutableState<AuthState> = mutableStateOf(AuthState())
    val reloadState: State<AuthState> = _reloadState

    private val _isEmailVerifyState: MutableState<AuthState> = mutableStateOf(AuthState())
    val isEmailVerifyState: State<AuthState> = _isEmailVerifyState

    private val _sendEmailVerificationState: MutableState<AuthState> = mutableStateOf(AuthState())
    val sendEmailVerification: State<AuthState> = _sendEmailVerificationState

    fun reloadEmail() {

        kotlin.runCatching {
            viewModelScope.launch {
                _reloadState.value = AuthState(isLoading = true)
                val result = authUseCase.reloadEmailUseCase()
                _reloadState.value = AuthState(isLoading = false, isSuccessful = result)
            }
        }
            .onFailure {
                _reloadState.value = AuthState(
                    isLoading = false,
                    isSuccessful = false,
                    error = it.message ?: "An unexpected error occurred.")
            }

    }

    fun verifyEmail() {

        kotlin.runCatching {
            viewModelScope.launch {
                _isEmailVerifyState.value = AuthState(isLoading = true)
                val result = authUseCase.isEmailVerifiedUseCase()
                _isEmailVerifyState.value = AuthState(isLoading = false, isSuccessful = result)
            }
        }.onFailure {
            _isEmailVerifyState.value = AuthState(
                isLoading = false,
                isSuccessful = false,
                error = it.message ?: "An unexpected error occurred")
        }
    }

    fun sendEmailVerification() {
        kotlin.runCatching {
            viewModelScope.launch {
                _sendEmailVerificationState.value = AuthState(isLoading = true)
                val result = authUseCase.sendEmailVerificationUseCase()
                _sendEmailVerificationState.value =
                    AuthState(isLoading = false, isSuccessful = result)
            }
        }.onFailure {
            _sendEmailVerificationState.value = AuthState(
                isLoading = false,
                isSuccessful = false,
                error = it.message ?: "An unexpected error occurred")
        }
    }

}