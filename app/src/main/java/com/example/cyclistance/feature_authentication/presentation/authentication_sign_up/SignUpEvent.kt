package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.ui.text.input.TextFieldValue
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEvent

sealed class SignUpEvent{
    object SignUp: SignUpEvent()
    data class EnteredEmail(val email: TextFieldValue) : SignUpEvent()
    data class EnteredName(val name: TextFieldValue) : SignUpEvent()
    data class EnteredPassword(val password: TextFieldValue) : SignUpEvent()
    data class EnteredConfirmPassword(val confirmPassword: TextFieldValue) : SignUpEvent()
    object ClearEmail: SignUpEvent()
    object ClearName:SignUpEvent()
    object ClearPassword: SignUpEvent()
    object TogglePasswordVisibility: SignUpEvent()
}
