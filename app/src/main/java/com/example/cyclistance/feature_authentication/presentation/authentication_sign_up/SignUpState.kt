package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up

import androidx.compose.ui.text.input.TextFieldValue

data class SignUpState(
    var email: TextFieldValue = TextFieldValue(""),
    var name:TextFieldValue = TextFieldValue(""),
    var password: TextFieldValue = TextFieldValue(""),
    var confirmPassword: TextFieldValue = TextFieldValue(""),
    var emailExceptionMessage: String = "",
    var nameExceptionMessage: String = "",
    var passwordExceptionMessage: String = "",
    var confirmPasswordExceptionMessage: String = "",
    var passwordVisibility: Boolean = false,
    var isLoading: Boolean = false,
)
