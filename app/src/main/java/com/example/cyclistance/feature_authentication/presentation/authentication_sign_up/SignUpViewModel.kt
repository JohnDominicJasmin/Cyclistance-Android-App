package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {


    private val _eventFlow: MutableSharedFlow<SignUpUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<SignUpUiEvent> = _eventFlow.asSharedFlow()

    private val _state: MutableState<SignUpState> = mutableStateOf(SignUpState())
    val state: State<SignUpState> = _state

    fun hasAccountSignedIn(): Boolean = authUseCase.hasAccountSignedInUseCase()


    fun onEvent(event: SignUpEvent){
        when(event){
            is SignUpEvent.SignUp -> {
                with(state.value){
                    viewModelScope.launch{
                        createUserWithEmailAndPassword(authModel = AuthModel(
                            email = email.text.trim(),
                            password = password.text.trim(),
                            confirmPassword = confirmPassword.text.trim()
                        ))
                    }
                }
            }
            is SignUpEvent.EnteredEmail -> {
                _state.value = state.value.copy(email = event.email, emailErrorMessage = "")
            }

            is SignUpEvent.EnteredPassword -> {
                _state.value = state.value.copy(password = event.password, passwordErrorMessage = "")
            }
            is SignUpEvent.EnteredConfirmPassword -> {
                _state.value = state.value.copy(confirmPassword = event.confirmPassword, confirmPasswordErrorMessage = "")
            }
            is SignUpEvent.ClearEmail -> {
                _state.value = state.value.copy(email = TextFieldValue(""))
            }
            is SignUpEvent.ClearPassword -> {
                _state.value = state.value.copy(password = TextFieldValue(""))
            }
            is SignUpEvent.TogglePasswordVisibility -> {
                _state.value =
                    state.value.copy(
                        passwordVisibility = !state.value.passwordVisibility)
            }



        }
    }


    private suspend fun createUserWithEmailAndPassword(authModel: AuthModel) {
            kotlin.runCatching {
                _state.value = state.value.copy(isLoading = true)
                authUseCase.createWithEmailAndPasswordUseCase(authModel)
            }.onSuccess { isAccountCreated ->
                _state.value = state.value.copy(isLoading = false)
                if(isAccountCreated) {
                    _eventFlow.emit(SignUpUiEvent.ShowEmailAuthScreen)
                }else{
                    _eventFlow.emit(SignUpUiEvent.ShowToastMessage("Sorry, something went wrong. Please try again."))
                }
            }.onFailure {exception ->
                _state.value = state.value.copy(isLoading = false)
                when(exception) {
                    is AuthExceptions.EmailException -> {
                        _state.value = state.value.copy(emailErrorMessage = exception.message ?: "Invalid Email.")
                    }
                    is AuthExceptions.PasswordException -> {
                        _state.value = state.value.copy(passwordErrorMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.ConfirmPasswordException -> {
                        _state.value = state.value.copy(confirmPasswordErrorMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.InternetException -> {
                        _eventFlow.emit(SignUpUiEvent.ShowNoInternetScreen)
                    }
                    is AuthExceptions.UserAlreadyExistsException -> {
                        _eventFlow.emit(
                            SignUpUiEvent.ShowAlertDialog(
                                title = exception.title,
                                description = exception.message ?: "This account is Already in Use.",
                                imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error,
                            ))
                    }
                    else ->{
                        Timber.e("${this@SignUpViewModel.javaClass.name}: ${exception.message}")
                    }

                }
        }
    }





}