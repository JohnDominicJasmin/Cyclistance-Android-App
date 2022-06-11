package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem


@Composable
fun SignInTextFieldsArea(
    state: SignInState,
    signInViewModel: SignInViewModel? = null,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)) {

    with(state) {

        Column(
            modifier = Modifier
                .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
                .fillMaxWidth(fraction = 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)) {


            EmailTextField(
                textFieldValue = email,
                emailExceptionMessage = emailErrorMessage,
                onValueChange = {
                    signInViewModel?.onEvent(SignInEvent.EnteredEmail(email = it))
                },
                clearIconOnClick = {
                    signInViewModel?.onEvent(SignInEvent.ClearEmail)
                })

            ConfirmPasswordTextField(
                passwordValue = password,
                passwordExceptionMessage = passwordErrorMessage,
                onValueChange = {
                    signInViewModel?.onEvent(SignInEvent.EnteredPassword(password = it))
                },
                keyboardActionOnDone = {
                    keyboardActionOnDone()
                },
                isPasswordVisible = passwordVisibility,
                passwordVisibilityIconOnClick = {
                    signInViewModel?.onEvent(SignInEvent.TogglePasswordVisibility)
                }
            )


        }
    }

}