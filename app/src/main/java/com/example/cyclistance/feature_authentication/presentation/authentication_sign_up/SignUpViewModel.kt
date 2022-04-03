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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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


    private val _eventFlow: MutableSharedFlow<SignUpEventResult> = MutableSharedFlow()
    val eventFlow: SharedFlow<SignUpEventResult> = _eventFlow.asSharedFlow()

    private val _state: MutableState<SignUpState> = mutableStateOf(SignUpState())
    val state: State<SignUpState> = _state

    fun hasAccountSignedIn(): Boolean = authUseCase.hasAccountSignedInUseCase()


    fun onEvent(event: SignUpEvent){
        when(event){
            is SignUpEvent.SignUp -> {
                with(state.value){
                    viewModelScope.launch{
                        createUserWithEmailAndPassword(authModel = AuthModel(
                            email = email.text,
                            password = password.text,
                            confirmPassword = confirmPassword.text
                        ))
                    }
                }
            }
            is SignUpEvent.EnteredEmail -> {
                _state.value = state.value.copy(email = event.email, emailExceptionMessage = "")
            }

            is SignUpEvent.EnteredPassword -> {
                _state.value = state.value.copy(password = event.password, passwordExceptionMessage = "")
            }
            is SignUpEvent.EnteredConfirmPassword -> {
                _state.value = state.value.copy(confirmPassword = event.confirmPassword, confirmPasswordExceptionMessage = "")
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
                    _eventFlow.emit(SignUpEventResult.ShowEmailAuthScreen)
                }else{
                    _eventFlow.emit(SignUpEventResult.ShowToastMessage("Sorry, something went wrong. Please try again."))
                }
            }.onFailure {exception ->
                _state.value = state.value.copy(isLoading = false)
                when(exception) {
                    is AuthExceptions.EmailException -> {
                        _state.value = state.value.copy(emailExceptionMessage = exception.message ?: "Invalid Email.")
                    }
                    is AuthExceptions.PasswordException -> {
                        _state.value = state.value.copy(passwordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.ConfirmPasswordException -> {
                        _state.value = state.value.copy(confirmPasswordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.InternetException -> {
                        _eventFlow.emit(SignUpEventResult.ShowNoInternetScreen)
                    }
                    is FirebaseAuthUserCollisionException -> {
                        _eventFlow.emit(
                            SignUpEventResult.ShowAlertDialog(
                                title = "Error",
                                description = exception.message ?: "User already exist.",
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