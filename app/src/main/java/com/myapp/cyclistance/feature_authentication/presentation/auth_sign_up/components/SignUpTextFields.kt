package com.myapp.cyclistance.feature_authentication.presentation.auth_sign_up.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.feature_authentication.presentation.auth_sign_up.state.SignUpState
import com.myapp.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains
import com.myapp.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.myapp.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.myapp.cyclistance.feature_authentication.presentation.common.PasswordTextField

@Composable
fun SignUpTextFieldsArea(
    state: SignUpState,
    email: TextFieldValue,
    emailErrorMessage: String,
    password: TextFieldValue,
    passwordErrorMessage: String,
    confirmPassword: TextFieldValue,
    confirmPasswordErrorMessage: String,
    passwordVisibility: Boolean,
    onValueChangeEmail: (TextFieldValue) -> Unit,
    onValueChangePassword: (TextFieldValue) -> Unit,
    onValueChangeConfirmPassword: (TextFieldValue) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit),
    onClickPasswordVisibility: () -> Unit) {


    with(state) {
        Column(
            modifier = Modifier
                .layoutId(AuthenticationConstrains.TEXT_FIELDS_ID)
                .fillMaxWidth(fraction = 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)) {


            EmailTextField(
                email = email,
                emailErrorMessage = emailErrorMessage,
                clearIconOnClick = {
                    onValueChangeEmail(TextFieldValue(""))
                },
                onValueChange = onValueChangeEmail,
                enabled = !isLoading)


            PasswordTextField(
                password = password,
                passwordExceptionMessage = passwordErrorMessage,
                clearIconOnClick = {
                    onValueChangePassword(TextFieldValue(""))
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


