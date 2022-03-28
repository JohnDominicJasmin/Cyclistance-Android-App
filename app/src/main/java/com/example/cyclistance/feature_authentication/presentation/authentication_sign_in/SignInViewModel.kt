package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.exceptions.AuthExceptions
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _signInWithEmailAndPasswordState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val signInWithEmailAndPasswordState: State<AuthState<Boolean>> = _signInWithEmailAndPasswordState


    private val _signInWithCredentialState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val signInWithCredentialState: State<AuthState<Boolean>> = _signInWithCredentialState

    fun clearState(){
        _signInWithEmailAndPasswordState.value = AuthState()
    }

    fun signInWithEmailAndPassword(authModel: AuthModel) {
        viewModelScope.launch {
            kotlin.runCatching {

                _signInWithEmailAndPasswordState.value =  AuthState(isLoading = true)
                 authUseCase.signInWithEmailAndPasswordUseCase(authModel)

            }.onSuccess {result ->
                _signInWithEmailAndPasswordState.value =  AuthState(isLoading = false, result = result)
            }.onFailure { exception ->

                when(exception){
                    is AuthExceptions.EmailException ->{
                        _signInWithEmailAndPasswordState.value = AuthState(emailExceptionMessage = exception.message ?: "Invalid Email.")
                    }
                    is AuthExceptions.PasswordException ->{
                        _signInWithEmailAndPasswordState.value = AuthState(passwordExceptionMessage = exception.message ?: "Invalid Password.")
                    }
                    is AuthExceptions.InternetException ->{
                        _signInWithEmailAndPasswordState.value = AuthState(internetExceptionMessage = exception.message ?: "No internet connection.")
                    }
                    is AuthExceptions.InvalidUserException ->{
                        _signInWithEmailAndPasswordState.value = AuthState(invalidUserExceptionMessage = exception.message ?: "Invalid User.")
                    }
                    is FirebaseAuthUserCollisionException ->{
                        _signInWithEmailAndPasswordState.value = AuthState(userCollisionExceptionMessage = exception.message ?: "An unexpected error occurred.")
                    }
                    else ->{
                        Timber.e("${this@SignInViewModel.javaClass.name}: ${exception.message}")
                    }

                }
            }
        }
    }


    fun signInWithCredential(authCredential: AuthCredential) {
        viewModelScope.launch {
            kotlin.runCatching {
                _signInWithCredentialState.value = AuthState(isLoading = true)
                authUseCase.signInWithCredentialUseCase(authCredential)
            }.onSuccess { result ->
                _signInWithCredentialState.value = AuthState(isLoading = false, result = result)
            }.onFailure { exception ->

                when (exception) {

                    is AuthExceptions.InternetException -> {
                        _signInWithCredentialState.value = AuthState(internetExceptionMessage = exception.message ?: "No Internet Connection.")
                    }
                    is AuthExceptions.ConflictFBTokenException -> {
                        _signInWithCredentialState.value = AuthState(conflictFBTokenExceptionMessage = exception.message ?: "Login Failed.")

                    }
                }
            }
        }
    }



}