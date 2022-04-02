package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _eventFlow: MutableSharedFlow<SignInEventResult> = MutableSharedFlow()
    val eventFlow: SharedFlow<SignInEventResult> = _eventFlow.asSharedFlow()


    private val _signInWithCredentialState: MutableSharedFlow<AuthState<Boolean>> =
        MutableSharedFlow()
    val signInWithCredentialState: SharedFlow<AuthState<Boolean>> =
        _signInWithCredentialState.asSharedFlow()

    private val _state: MutableState<SignInState> = mutableStateOf(SignInState())
    val state: State<SignInState> = _state


    fun onEvent(event: SignInEvent) {


        when (event) {
            is SignInEvent.SignInFacebook -> { /*TODO*/ }

            is SignInEvent.SignInGoogle -> { /*TODO*/ }

            is SignInEvent.SignInDefault -> {

                with(state.value) {
                    viewModelScope.launch {
                        signInWithEmailAndPassword(
                            authModel = AuthModel(
                                email = email.text,
                                password = password.text))
                    }
                }
            }
            is SignInEvent.EnteredEmail -> {
                _state.value = state.value.copy(email = event.email, emailExceptionMessage = "")

            }
            is SignInEvent.EnteredPassword -> {
                _state.value = state.value.copy(password = event.password, passwordExceptionMessage = "")

            }
            is SignInEvent.ClearEmail -> {
                _state.value = state.value.copy(email = TextFieldValue(""))
            }
            is SignInEvent.TogglePasswordVisibility -> {
                _state.value =
                    state.value.copy(
                        passwordVisibility = !state.value.passwordVisibility)
            }

        }
    }


    private suspend fun signInWithEmailAndPassword(authModel: AuthModel) {

        kotlin.runCatching {
            _state.value = state.value.copy(isLoading = true)
            authUseCase.signInWithEmailAndPasswordUseCase(authModel)

        }.onSuccess { isSignedIn ->
            _state.value = state.value.copy(isLoading = false)
            if (isSignedIn) {
                _eventFlow.emit(SignInEventResult.RefreshEmail)
            }
        }.onFailure { exception ->
            _state.value = state.value.copy(isLoading = false)
            when (exception) {
                is AuthExceptions.EmailException -> {
                    _state.value = state.value.copy(
                        emailExceptionMessage = exception.message ?: "Invalid Email.")
                }
                is AuthExceptions.PasswordException -> {
                    _state.value = state.value.copy(passwordExceptionMessage = exception.message ?: "Invalid Password.")
                }
                is AuthExceptions.InternetException -> {
                    _eventFlow.emit(SignInEventResult.ShowInternetScreen)
                }
                is AuthExceptions.InvalidUserException -> {
                    _eventFlow.emit(
                        SignInEventResult.ShowAlertDialog(
                            title = "Error",
                            description = exception.message ?: "Invalid User",
                            imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error,
                        ))
                }
                is FirebaseAuthUserCollisionException -> {
                    _eventFlow.emit(
                        SignInEventResult.ShowAlertDialog(
                            title = "Error",
                            description = exception.message ?: "User already exist.",
                            imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error,
                        ))
                }
                else -> {
                    Timber.e("${this@SignInViewModel.javaClass.name}: ${exception.message}")
                }

            }
        }
    }


    fun signInWithCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            kotlin.runCatching {
                _signInWithCredentialState.emit(AuthState(isLoading = true))
                authUseCase.signInWithCredentialUseCase(authCredential)
            }.onSuccess { result ->
                _signInWithCredentialState.emit(AuthState(isLoading = false, result = result))
            }.onFailure { exception ->

                when (exception) {
                    //TODO: Change this later
                    is AuthExceptions.InternetException -> {
                        _signInWithCredentialState.emit(
                            AuthState(
                                internetExceptionMessage = exception.message
                                                           ?: "No Internet Connection."))
                    }
                    is AuthExceptions.ConflictFBTokenException -> {
                        _signInWithCredentialState.emit(
                            AuthState(
                                conflictFBTokenExceptionMessage = exception.message
                                                                  ?: "Login Failed."))

                    }
                }
            }
        }
    }


}