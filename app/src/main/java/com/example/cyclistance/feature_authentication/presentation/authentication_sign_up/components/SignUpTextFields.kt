package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpState
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.PasswordTextField

@Composable
fun SignUpTextFieldsArea(
    state: SignUpState,
    email: String,
    emailErrorMessage: String,
    password: String,
    passwordErrorMessage: String,
    confirmPassword: String,
    confirmPasswordErrorMessage: String,
    passwordVisibility: Boolean,
    focusRequester: FocusRequester,
    onValueChangeEmail: (String) -> Unit,
    onValueChangePassword: (String) -> Unit,
    onValueChangeConfirmPassword: (String) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit),
    onClickPasswordVisibility: () -> Unit) {


    with(state) {
        Column(
            modifier = Modifier
                .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
                .fillMaxWidth(fraction = 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)) {


            EmailTextField(
                focusRequester = focusRequester,
                email = email,
                emailErrorMessage = emailErrorMessage,
                clearIconOnClick = {
                 onValueChangeEmail("")
                },
                onValueChange = onValueChangeEmail,
                enabled = !isLoading)


            PasswordTextField(
                password = password,
                passwordExceptionMessage = passwordErrorMessage,
                clearIconOnClick = {
                    onValueChangePassword("")
                },
                onValueChange = onValueChangePassword,
                enabled = !isLoading,

                )

            ConfirmPasswordTextField(
                password = confirmPassword,
                passwordErrorMessage = confirmPasswordErrorMessage,
                onValueChange = onValueChangeConfirmPassword,
                keyboardActionOnDone = keyboardActionOnDone,
                isPasswordVisible = passwordVisibility,
                passwordVisibilityIconOnClick = onClickPasswordVisibility,
                enabled = !isLoading
                )

        }
    }

}


