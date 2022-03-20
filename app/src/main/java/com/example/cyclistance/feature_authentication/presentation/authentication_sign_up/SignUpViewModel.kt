package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.example.cyclistance.feature_authentication.presentation.common.InputResultState
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _createAccountState: MutableState<InputResultState<Boolean>> = mutableStateOf(InputResultState<Boolean>())
    val createAccountState: State<InputResultState<Boolean>> = _createAccountState


    fun clearState(){
        _createAccountState.value = InputResultState()
    }

    fun createUserWithEmailAndPassword(authModel: AuthModel) {
        viewModelScope.launch {
            kotlin.runCatching {
                _createAccountState.value = InputResultState(authState = AuthState(isLoading = true))
                authUseCase.createWithEmailAndPasswordUseCase(authModel)
            }.onSuccess {result ->
                _createAccountState.value = InputResultState(authState = AuthState(isLoading = false, result = result))
            }.onFailure {exception ->
                when(exception) {
                    is AuthExceptions.EmailException -> {
                        _createAccountState.value = InputResultState(emailExceptionMessage = exception.message ?: "Invalid Email.")
                    }
                    is AuthExceptions.PasswordException -> {
                        _createAccountState.value = InputResultState(passwordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.ConfirmPasswordException -> {
                        _createAccountState.value = InputResultState(confirmPasswordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.InternetException -> {
                        _createAccountState.value = InputResultState(internetExceptionMessage = exception.message ?: "No internet connection.")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        _createAccountState.value = InputResultState(userCollisionExceptionMessage = exception.message ?: "An unexpected error occurred.")
                    }
                    else ->{
                        Timber.e("${this@SignUpViewModel.javaClass.name}: ${exception.message}")
                    }

                }
            }
        }
    }

    fun saveAccount() {
        authUseCase.registerAccountUseCase()
    }

    fun hasAccountSignedIn(): State<AuthState<Boolean>> =
        mutableStateOf(AuthState<Boolean>(result = authUseCase.hasAccountSignedInUseCase()))


}