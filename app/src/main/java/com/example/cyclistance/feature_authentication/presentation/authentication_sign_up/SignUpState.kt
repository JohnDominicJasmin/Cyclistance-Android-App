package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.ui.text.input.TextFieldValue

data class SignUpState(
    var email: TextFieldValue = TextFieldValue(""),
    var password: TextFieldValue = TextFieldValue(""),
    var confirmPassword: TextFieldValue = TextFieldValue(""),
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var confirmPasswordErrorMessage: String = "",
    var passwordVisibility: Boolean = false,
    var isLoading: Boolean = false,
)
