package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.model.AuthModel
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase) : ViewModel() {

    private val _createAccountState: MutableState<SignUpState<Boolean>> =
        mutableStateOf(SignUpState<Boolean>())
    val createAccountState: State<SignUpState<Boolean>> = _createAccountState

    fun createUserWithEmailAndPassword(authModel: AuthModel) {
        viewModelScope.launch {
            kotlin.runCatching {
                _createAccountState.value = AuthState(isLoading = true)
                val result = authUseCase.createWithEmailAndPasswordUseCase(authModel)
                _createAccountState.value = AuthState(isLoading = false, result = result)

            }.onFailure {
                _createAccountState.value = SignUpState(authState = AuthState(
                    isLoading = false,
                    result = false,
                    error = it.message ?: "An unexpected error occurred."))
            }
        }
    }

    fun registerAccount() {
        authUseCase.registerAccountUseCase()
    }

    fun hasAccountSignedIn() =
        mutableStateOf(AuthState<Boolean>(result = authUseCase.hasAccountSignedInUseCase()))


}