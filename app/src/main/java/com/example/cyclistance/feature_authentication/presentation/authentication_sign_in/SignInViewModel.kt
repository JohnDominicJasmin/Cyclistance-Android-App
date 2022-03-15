package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _signInWithEmailAndPasswordState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val signInWithEmailAndPasswordState: State<AuthState<Boolean>> = _signInWithEmailAndPasswordState


    private val _signInWithCredentialState: MutableState<AuthState<Boolean>> = mutableStateOf(AuthState<Boolean>())
    val signInWithCredentialState: State<AuthState<Boolean>> = _signInWithCredentialState



    fun signInWithEmailAndPassword(authModel: AuthModel) {
        viewModelScope.launch {
            kotlin.runCatching {

                _signInWithEmailAndPasswordState.value = AuthState(isLoading = true)
                val result = authUseCase.signInWithEmailAndPasswordUseCase(authModel)
                _signInWithEmailAndPasswordState.value = AuthState(isLoading = false, result = result)
            }.onFailure {
                _signInWithEmailAndPasswordState.value = AuthState(isLoading = false, error = it.message?:"An unexpected error occurred.")
            }
        }
    }


    fun signInWithCredential(authCredential: AuthCredential){
        viewModelScope.launch {
            kotlin.runCatching {
                _signInWithCredentialState.value = AuthState(isLoading = true)
                val result = authUseCase.signInWithCredentialUseCase(authCredential)
                _signInWithCredentialState.value = AuthState(isLoading = false, result = result)
            }.onFailure {
                _signInWithCredentialState.value = AuthState(isLoading = false, error = it.message?:"An unexpected error occurred.")
            }
        }
    }
}