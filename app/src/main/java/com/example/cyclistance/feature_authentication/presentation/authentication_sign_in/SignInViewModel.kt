package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import android.content.Intent
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.core.utils.AuthConstants.FACEBOOK_CONNECTION_FAILURE
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.domain.util.ActivityResultCallbackI
import com.example.cyclistance.feature_authentication.domain.util.findActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel(), ActivityResultCallbackI {

    private var callbackManager = CallbackManager.Factory.create()

    private val _state: MutableStateFlow<SignInState> = MutableStateFlow(SignInState())
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
                event.context.findActivity()?.let { activity ->
                    LoginManager.getInstance().logInWithReadPermissions(activity, listOf("email", "public_profile"))
                }
            }

            is SignInEvent.SignInGoogle -> {
                signInWithCredential(event.authCredential)
            }

            is SignInEvent.SignInDefault -> {

                with(state.value) {
                    viewModelScope.launch {
                        signInWithEmailAndPassword(
                            authModel = AuthModel(
                                email = email.text.trim(),
                                password = password.text.trim()))
                    }
                }
            }
            is SignInEvent.EnteredEmail -> {
                _state.update { it.copy(email = event.email, emailErrorMessage = "") }
            }
            is SignInEvent.EnteredPassword -> {
                _state.update { it.copy(password = event.password, passwordErrorMessage = "") }
            }
            is SignInEvent.ClearEmail -> {
                _state.update { it.copy(email = TextFieldValue("")) }
            }
            is SignInEvent.TogglePasswordVisibility -> {
                _state.update { it.copy( passwordVisibility = !state.value.passwordVisibility) }
            }

        }
    }


    private suspend fun signInWithEmailAndPassword(authModel: AuthModel) {

        runCatching {
            _state.update { it.copy(isLoading = true) }
            authUseCase.signInWithEmailAndPasswordUseCase(authModel)

        }.onSuccess { isSignedIn ->
            _state.update { it.copy(isLoading = false) }
            if (isSignedIn) {
                _eventFlow.emit(SignInUiEvent.RefreshEmail)
            }else{
                _eventFlow.emit(SignInUiEvent.ShowToastMessage("Sorry, something went wrong. Please try again."))
            }
        }.onFailure { exception ->
            _state.update { it.copy(isLoading = false) }
            when (exception) {
                is AuthExceptions.EmailException -> {
                    _state.update { it.copy(emailErrorMessage = exception.message ?: "Email is Invalid.") }
                }
                is AuthExceptions.PasswordException -> {
                    _state.update { it.copy(passwordErrorMessage = exception.message ?: "Password is Invalid.") }
                }
                is AuthExceptions.InternetException -> {
                    _eventFlow.emit(SignInUiEvent.ShowNoInternetScreen)
                }
                is AuthExceptions.TooManyRequestsException -> {
                    _eventFlow.emit(
                        SignInUiEvent.ShowAlertDialog(
                            title = exception.title,
                            description = exception.message ?: "You have been blocked for too many failed attempts. Please try again later.",
                            imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error,
                        ))
                }
                else -> {
                    Timber.e("${this@SignInViewModel.javaClass.name}: ${exception.message}")
                }
            }
        }
    }


    private fun signInWithCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            kotlin.runCatching {

                _state.update { it.copy(isLoading = true) }
                authUseCase.signInWithCredentialUseCase(authCredential)
            }.onSuccess { isSuccess ->
                _state.update { it.copy(isLoading = false) }
                if (isSuccess) {
                    _eventFlow.emit(SignInUiEvent.ShowMappingScreen)
                }
            }.onFailure { exception ->
                _state.update { it.copy(isLoading = false) }
                when (exception) {
                    is AuthExceptions.InternetException -> {
                        _eventFlow.emit(SignInUiEvent.ShowNoInternetScreen)
                    }
                    is AuthExceptions.ConflictFBTokenException -> {
                        removeFacebookUserAccountPreviousToken()
                        _eventFlow.emit(SignInUiEvent.ShowAlertDialog(title = "Error", description = exception.message?:"Sorry, something went wrong. Please try again.", imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error ))


                    }
                }
            }
        }
    }

    private fun registerFacebookSignInCallback() {
        LoginManager.getInstance().registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    _state.update { it.copy(isLoading = true) }
                    signInWithCredential(authCredential = FacebookAuthProvider.getCredential(result.accessToken.token))
                }

                override fun onCancel() {
                    _state.update { it.copy(isLoading = false) }
                    Timber.e("facebook:onCancel");
                }

                override fun onError(error: FacebookException) {
                    _state.update { it.copy(isLoading = false) }
                    viewModelScope.launch {
                        handleFacebookSignInException(error)
                    }
                }
            }
        )
    }


   private suspend fun handleFacebookSignInException(error: Exception) {
        if (error.message == FACEBOOK_CONNECTION_FAILURE) {
            _eventFlow.emit(SignInUiEvent.ShowNoInternetScreen)
            return
        }
        error.message?.let { errorMessage ->
            _eventFlow.emit(SignInUiEvent.ShowAlertDialog(
                    title = "Error",
                    description = errorMessage,
                    imageResId = io.github.farhanroy.composeawesomedialog.R.raw.error))

            removeFacebookUserAccountPreviousToken()
        }
    }

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