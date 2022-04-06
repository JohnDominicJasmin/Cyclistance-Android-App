package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in

import androidx.compose.ui.text.input.TextFieldValue

data class SignInState(
    var email: TextFieldValue = TextFieldValue(""),
    var password: TextFieldValue = TextFieldValue(""),
    var emailErrorMessage: String = "",
    var passwordErrorMessage: String = "",
    var passwordVisibility: Boolean = false,
    var isLoading: Boolean = false,
)
