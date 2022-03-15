package com.example.cyclistance.feature_mapping.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cyclistance.feature_authentication.domain.use_case.AuthenticationUseCase
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MappingViewModel @Inject constructor(
    private val authUseCase: AuthenticationUseCase):ViewModel() {

    private val _emailState: MutableState<AuthState<String>> = mutableStateOf(AuthState<String>())
    val emailState: State<AuthState<String>> = _emailState

    private val _nameState: MutableState<AuthState<String>> = mutableStateOf(AuthState<String>())
    val nameState: State<AuthState<String>> = _nameState


    fun signOutAccount() = authUseCase.signOutUseCase()

    fun getName() =
        mutableStateOf(AuthState<String>(result = authUseCase.getNameUseCase()))

    fun getEmail() =
        mutableStateOf(AuthState<String>(result = authUseCase.getEmailUseCase()))


}