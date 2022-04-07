package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpEvent
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpViewModel
import com.example.cyclistance.feature_authentication.presentation.common.*

@Composable
fun SignUpTextFieldsArea(
    state: SignUpState,
    signUpViewModel: SignUpViewModel,
    keyboardActionOnDone: () -> Unit) {


    with(state) {
        Column(
            modifier = Modifier
                .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
                .fillMaxWidth(fraction = 0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(13.dp)) {


            EmailTextField(
                email = email,
                emailExceptionMessage = emailErrorMessage,
                clearIconOnClick = {
                    signUpViewModel.onEvent(SignUpEvent.ClearEmail)
                },
                onValueChange = {
                    signUpViewModel.onEvent(SignUpEvent.EnteredEmail(email = it))
                })


            PasswordTextField(
                password = password,
                passwordExceptionMessage = passwordErrorMessage,
                clearIconOnClick = {
                    signUpViewModel.onEvent(SignUpEvent.ClearPassword)
                },
                onValueChange = {
                    signUpViewModel.onEvent(SignUpEvent.EnteredPassword(password = it))
                })

            ConfirmPasswordTextField(
                passwordValue = confirmPassword,
                passwordExceptionMessage = confirmPasswordErrorMessage,
                onValueChange = {
                    signUpViewModel.onEvent(SignUpEvent.EnteredConfirmPassword(confirmPassword = it))
                },
                keyboardActionOnDone = {
                    keyboardActionOnDone()
                },
                passwordVisibility = passwordVisibility,
                passwordVisibilityIconOnClick = {
                    signUpViewModel.onEvent(SignUpEvent.TogglePasswordVisibility)
                })

        }
    }

}