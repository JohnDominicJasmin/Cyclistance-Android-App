package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.R
import com.example.cyclistance.core.utils.constants.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.core.utils.constants.AuthConstants.SIGN_IN_VM_STATE_KEY
import com.example.cyclistance.feature_alert_dialog.domain.model.AlertDialogModel
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.model.SignInCredential
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackI
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authUseCase: AuthenticationUseCase) : ViewModel(), ActivityResultCallbackI {

    private var job: Job? = null
    private var callbackManager = CallbackManager.Factory.create()

    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(savedStateHandle[SIGN_IN_VM_STATE_KEY] ?: SignInState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SignInUiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<SignInUiEvent> = _eventFlow.asSharedFlow()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        return callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    init {
        registerFacebookSignInCallback()
        removeFacebookUserAccountPreviousToken()
    }

    fun onEvent(event: SignInEvent) {


        when (event) {
            is SignInEvent.SignInFacebook -> {

                _state.update { it.copy(isLoading = true) }
                event.activity?.let {
                    LoginManager.getInstance()
                        .logInWithReadPermissions(it, listOf("email", "public_profile", "user_friends"))
                }
            }

            is SignInEvent.DismissNoInternetDialog -> {
                _state.update { it.copy(hasInternet = true) }
            }


            is SignInEvent.DismissAlertDialog -> {
                _state.update { it.copy(alertDialogModel = AlertDialogModel()) }
            }

            is SignInEvent.SignInGoogle -> {
                signInWithCredential(event.authCredential)
            }

            is SignInEvent.SignInDefault -> {
                signInDefault()
            }
            is SignInEvent.EnterEmail -> {
                _state.update { it.copy(email = event.email, emailErrorMessage = "") }
            }
            is SignInEvent.EnterPassword -> {
                _state.update { it.copy(password = event.password, passwordErrorMessage = "") }
            }
            is SignInEvent.TogglePasswordVisibility -> {
                _state.update { it.copy(passwordVisibility = !state.value.passwordVisibility) }
            }

        }
        savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
    }


    private fun signInDefault() {

        viewModelScope.launch {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                with(state.value) {
                    authUseCase.signInWithEmailAndPasswordUseCase(
                        authModel = AuthModel(
                            email = email.trim(),
                            password = password.trim()))
                }

            }.onSuccess { isSignedIn ->
                _state.update { it.copy(isLoading = false) }
                if (isSignedIn) {
                    _eventFlow.emit(SignInUiEvent.RefreshEmail)
                } else {
                    _eventFlow.emit(SignInUiEvent.ShowToastMessage("Sorry, something went wrong. Please try again."))
                    // TODO: move to Constants
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                handleException(exception)
            }
        }.invokeOnCompletion {
            savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
        }
    }



    private fun signInWithCredential(authCredential: SignInCredential) {
        job?.cancel()
        job = viewModelScope.launch {
            runCatching {
                _state.update { it.copy(isLoading = true) }
                authUseCase.signInWithCredentialUseCase(authCredential)
            }.onSuccess { isSuccess ->
                _state.update { it.copy(isLoading = false) }
                if (isSuccess) {
                    _eventFlow.emit(SignInUiEvent.ShowMappingScreen)
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                handleException(exception)
            }
        }.apply {
            invokeOnCompletion {
                savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
            }
        }

    }

    private fun handleException(exception: Throwable){
        when (exception) {
            is AuthExceptions.EmailException -> {
                _state.update {
                    it.copy(
                        emailErrorMessage = exception.message ?: "Email is Invalid.")
                    // TODO: move to Constants
                }
            }
            is AuthExceptions.PasswordException -> {
                _state.update {
                    it.copy(
                        passwordErrorMessage = exception.message ?: "Password is Invalid.")
                    // TODO: move to Constants
                }
            }

            is AuthExceptions.TooManyRequestsException -> {
                _state.update {
                    it.copy(
                        alertDialogModel = AlertDialogModel(
                            title = exception.title,
                            description = exception.message ?: "You have been blocked temporarily for too many failed attempts. Please try again later.",
                            // TODO: move to Constants
                            icon = R.raw.error,
                        ))
                }
            }

            is AuthExceptions.NetworkException -> {
                _state.update { it.copy(hasInternet = false) }
            }
            is AuthExceptions.ConflictFBTokenException -> {
                removeFacebookUserAccountPreviousToken()
                _state.update {
                    it.copy(
                        alertDialogModel = AlertDialogModel(
                            title = "Error",
                            description = exception.message ?: "Sorry, something went wrong. Please try again.",
                            icon = R.raw.error))
                    // TODO: move to Constants
                }
            }

            else -> {
                Timber.e("${this@SignInViewModel.javaClass.name}: ${exception.message}")
            }
        }
        savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
    }

    // TODO: move to repository
    private fun registerFacebookSignInCallback() {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    _state.update { it.copy(isLoading = true) }
                    signInWithCredential(authCredential = SignInCredential.Facebook(providerToken =  result.accessToken.token))
                    savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
                }

                override fun onCancel() {
                    _state.update { it.copy(isLoading = false) }
                    savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
                    Timber.e("facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    _state.update { it.copy(isLoading = false) }
                    viewModelScope.launch {
                        error.handleFacebookSignInException()
                    }.invokeOnCompletion {
                        savedStateHandle[SIGN_IN_VM_STATE_KEY] = state.value
                    }
                }
            }
        )
    }


    private fun Exception.handleFacebookSignInException() {
        if (message == FACEBOOK_CONNECTION_FAILURE) {
            _state.update { it.copy(hasInternet = false) }
            return
        }
        message?.let { errorMessage ->
            _state.update {
                it.copy(alertDialogModel = AlertDialogModel(
                        title = "Error",
                        description = errorMessage,
                        icon = R.raw.error))
            }
            removeFacebookUserAccountPreviousToken()
        }
    }

    //todo move this to repository
    private fun removeFacebookUserAccountPreviousToken() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut()
        }
    }

    override fun onCleared() {
        super.onCleared()
        LoginManager.getInstance().unregisterCallback(callbackManager)
    }
}