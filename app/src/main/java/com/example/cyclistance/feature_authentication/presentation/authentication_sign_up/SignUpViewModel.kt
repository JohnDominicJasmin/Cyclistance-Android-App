package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState())
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

    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.SignUp -> {
                with(state.value) {
                    viewModelScope.launch {
                        createUserWithEmailAndPassword(
                            authModel = AuthModel(
                                email = email.trim(),
                                password = password.trim(),
                                confirmPassword = confirmPassword.trim()
                            ))
                    }
                }
            }
            is SignUpEvent.DismissNoInternetScreen -> {
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
    }


    private suspend fun createUserWithEmailAndPassword(authModel: AuthModel) {
        runCatching {
            _state.update { it.copy(isLoading = true) }
            authUseCase.createWithEmailAndPasswordUseCase(authModel)
        }.onSuccess { isAccountCreated ->
            _state.update { it.copy(isLoading = false) }
            if (isAccountCreated) {
                _eventFlow.emit(SignUpUiEvent.ShowEmailAuthScreen)
            } else {
                _eventFlow.emit(SignUpUiEvent.ShowToastMessage("Sorry, something went wrong. Please try again."))
            }
        }.onFailure { exception ->
            _state.update { it.copy(isLoading = false) }
            handleException(exception)
        }
    }

    private fun handleException(exception: Throwable) {
        when (exception) {
            is AuthExceptions.EmailException -> {
                _state.update { it.copy(emailErrorMessage = exception.message ?: "Invalid Email.") }
            }
            is AuthExceptions.PasswordException -> {
                _state.update {
                    it.copy(
                        passwordErrorMessage = exception.message ?: "Invalid Password.")
                }
            }
            is AuthExceptions.ConfirmPasswordException -> {
                _state.update {
                    it.copy(
                        confirmPasswordErrorMessage = exception.message ?: "Invalid Password.")
                }
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
                            icon = io.github.farhanroy.composeawesomedialog.R.raw.error,
                        ))
                }
            }
            else -> {
                Timber.e("${this@SignUpViewModel.javaClass.name}: ${exception.message}")
            }

        }
    }


}