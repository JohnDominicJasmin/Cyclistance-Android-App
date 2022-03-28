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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _createAccountState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val createAccountState: State<AuthState<Boolean>> = _createAccountState

     fun hasAccountSignedIn(): Boolean = authUseCase.hasAccountSignedInUseCase()

    fun clearState(){
        _createAccountState.value = AuthState()
    }

    fun createUserWithEmailAndPassword(authModel: AuthModel) {
        viewModelScope.launch {
            kotlin.runCatching {
                _createAccountState.value =  AuthState(isLoading = true)
                authUseCase.createWithEmailAndPasswordUseCase(authModel)
            }.onSuccess {result ->
                _createAccountState.value =  AuthState(isLoading = false, result = result)
            }.onFailure {exception ->
                when(exception) {
                    is AuthExceptions.EmailException -> {
                        _createAccountState.value = AuthState(emailExceptionMessage = exception.message ?: "Invalid Email.")
                    }
                    is AuthExceptions.PasswordException -> {
                        _createAccountState.value = AuthState(passwordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.ConfirmPasswordException -> {
                        _createAccountState.value = AuthState(confirmPasswordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.InternetException -> {
                        _createAccountState.value = AuthState(internetExceptionMessage = exception.message ?: "No Internet Connection.")
                    }
                    is FirebaseAuthUserCollisionException -> {
                        _createAccountState.value = AuthState(userCollisionExceptionMessage = exception.message ?: "An unexpected error occurred.")
                    }
                    else ->{
                        Timber.e("${this@SignUpViewModel.javaClass.name}: ${exception.message}")
                    }

                }
            }
        }
    }





}