package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.AuthConstants.SIGN_UP_VM_STATE_KEY
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase,
    private val defaultDispatcher: CoroutineDispatcher
    ) : ViewModel() {

    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(savedStateHandle[SIGN_UP_VM_STATE_KEY] ?: SignUpState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SignUpUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<SignUpUiEvent> = _eventFlow.asSharedFlow()

    init {
        _state.update {
            it.copy(
                hasAccountSignedIn = authUseCase.hasAccountSignedInUseCase(),
                savedAccountEmail = authUseCase.getEmailUseCase() ?: "",
            )
        }
        savedStateHandle[SIGN_UP_VM_STATE_KEY] = state.value

    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUp -> {
                signUp()
            }

            is SignUpEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
            }

            is SignUpEvent.EnterEmail -> {
                _state.update { it.copy(email = event.email, emailErrorMessage = "") }
            }
            is SignUpEvent.EnterPassword -> {
                _state.update { it.copy(password = event.password, passwordErrorMessage = "") }
            }
            is SignUpEvent.EnterConfirmPassword -> {
                _state.update {
                    it.copy(
                        confirmPassword = event.confirmPassword,
                        confirmPasswordErrorMessage = "")
                }
            }
            is SignUpEvent.DismissAlertDialog -> {
                _state.update { it.copy(alertDialogModel = AlertDialogModel()) }
            }
            is SignUpEvent.SignOut -> {
                authUseCase.signOutUseCase()
            }
            is SignUpEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }


        }
        savedStateHandle[SIGN_UP_VM_STATE_KEY] = state.value
    }


    private fun signUp() {
        viewModelScope.launch(context = defaultDispatcher) {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                with(state.value) {
                    authUseCase.createWithEmailAndPasswordUseCase(
                        authModel = AuthModel(
                            email = email.trim(),
                            password = password.trim(),
                            confirmPassword = confirmPassword.trim()
                        ))
                }
            }.onSuccess { isAccountCreated ->
                _state.update { it.copy(isLoading = false) }
                if (isAccountCreated) {
                    _eventFlow.emit(SignUpUiEvent.SignUpSuccess)
                } else {
                    _eventFlow.emit(SignUpUiEvent.CreateAccountFailed())
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                handleException(exception)
            }
        }.invokeOnCompletion {
            savedStateHandle[SIGN_UP_VM_STATE_KEY] = state.value
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is AuthExceptions.EmailException -> {
                _state.update { it.copy(emailErrorMessage = exception.message ?: "Invalid Email.") }
                // TODO: Move to constants
            }
            is AuthExceptions.PasswordException -> {
                _state.update {
                    it.copy(
                        passwordErrorMessage = exception.message ?: "Invalid Password.")
                }
                // TODO: Move to constants
            }
            is AuthExceptions.ConfirmPasswordException -> {
                _state.update {
                    it.copy(
                        confirmPasswordErrorMessage = exception.message ?: "Invalid Password.")
                }
                // TODO: Move to constants
            }

            is AuthExceptions.NetworkException -> {
                _state.update { it.copy(hasInternet = false) }
            }
            is AuthExceptions.UserAlreadyExistsException -> {

                _state.update {
                    it.copy(
                        alertDialogModel = AlertDialogModel(
                            title = exception.title,
                            description = exception.message ?: "This account is Already in Use.",
                            icon = R.raw.error,
                        ))
                }
                // TODO: Move to constants
            }
            else -> {
                Timber.e("${this@SignUpViewModel.javaClass.name}: ${exception.message}")
            }
        }
        savedStateHandle[SIGN_UP_VM_STATE_KEY] = state.value
    }

}