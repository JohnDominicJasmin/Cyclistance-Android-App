package com.example.cyclistance.feature_authentication.presentation.auth_sign_in.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.auth_sign_in.state.SignInState
import com.example.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField


@Composable
fun SignInTextFieldsArea(
    modifier: Modifier = Modifier,
    state: SignInState,
    email: TextFieldValue,
    emailErrorMessage: String,
    password: TextFieldValue,
    passwordErrorMessage: String,
    passwordVisible: Boolean,
    onValueChangeEmail: (TextFieldValue) -> Unit,
    onValueChangePassword: (TextFieldValue) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit),
    onClickPasswordVisibility: () -> Unit) {


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {


        EmailTextField(
            email = email,
            emailErrorMessage = emailErrorMessage,
            onValueChange = onValueChangeEmail,
            clearIconOnClick = {
                onValueChangeEmail(TextFieldValue(""))
            },
            enabled = !state.isLoading)

        ConfirmPasswordTextField(
            password = password,
            passwordErrorMessage = passwordErrorMessage,
            onValueChange = onValueChangePassword,
            keyboardActionOnDone = keyboardActionOnDone,
            isPasswordVisible = passwordVisible,
            passwordVisibilityIconOnClick = onClickPasswordVisibility,
            enabled = !state.isLoading
        )


    }

}