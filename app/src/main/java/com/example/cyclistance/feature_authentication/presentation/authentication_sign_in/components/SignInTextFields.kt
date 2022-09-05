package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState
import com.example.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem


@Composable
fun SignInTextFieldsArea(
    state: SignInState,
    focusRequester: FocusRequester,
    email: TextFieldValue,
    password: TextFieldValue,
    onEmailValueChange: (TextFieldValue) -> Unit,
    onPasswordValueChange: (TextFieldValue) -> Unit,
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
                textFieldValue = email,
                emailExceptionMessage = emailErrorMessage,
                onValueChange = onEmailValueChange,
                clearIconOnClick = {
                    onEmailValueChange(TextFieldValue())
                })

            ConfirmPasswordTextField(
                passwordValue = password,
                passwordExceptionMessage = passwordErrorMessage,
                onValueChange = onPasswordValueChange,
                keyboardActionOnDone = keyboardActionOnDone,
                isPasswordVisible = passwordVisibility,
                passwordVisibilityIconOnClick = passwordVisibilityOnClick
            )


        }
    }

}