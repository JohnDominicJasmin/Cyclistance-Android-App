package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password

import androidx.lifecycle.ViewModel
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.event.ForgotPasswordEvent
import com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.state.ForgotPasswordState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow


class ForgotPasswordViewModel: ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ForgotPasswordEvent>()
    val event = _event.asSharedFlow()
}