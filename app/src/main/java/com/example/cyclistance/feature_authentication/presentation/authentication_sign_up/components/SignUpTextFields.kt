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
import com.example.cyclistance.feature_authentication.presentation.common.*

@Composable
fun SignUpTextFieldsArea(
    state: SignUpState,
    focusRequester: FocusRequester,
    onEmailValueChange: (String) -> Unit,
    onPasswordValueChange: (String) -> Unit,
    onConfirmPasswordValueChange: (String) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit),
    passwordVisibilityOnClick: () -> Unit) {


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
                emailExceptionMessage = emailErrorMessage,
                clearIconOnClick = {
                 onEmailValueChange("")
                },
                onValueChange = onEmailValueChange)


            PasswordTextField(
                password = password,
                passwordExceptionMessage = passwordErrorMessage,
                clearIconOnClick = {
                    onPasswordValueChange("")
                },
                onValueChange = onPasswordValueChange)

            ConfirmPasswordTextField(
                password = confirmPassword,
                passwordExceptionMessage = confirmPasswordErrorMessage,
                onValueChange = onConfirmPasswordValueChange,
                keyboardActionOnDone = keyboardActionOnDone,
                isPasswordVisible = passwordVisibility,
                passwordVisibilityIconOnClick = passwordVisibilityOnClick)

        }
    }

}


