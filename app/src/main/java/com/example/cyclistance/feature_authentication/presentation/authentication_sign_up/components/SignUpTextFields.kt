package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

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
import com.example.cyclistance.feature_authentication.presentation.common.*

@Composable
fun SignUpTextFieldsArea(
    email: TextFieldValue,
    emailOnValueChange: (TextFieldValue) -> Unit,
    emailExceptionMessage: String,
    emailClearIconOnClick: () -> Unit,


    password: TextFieldValue,
    passwordOnValueChange: (TextFieldValue) -> Unit,
    passwordExceptionMessage: String,
    passwordClearIconOnClick: () -> Unit,

    confirmPassword: TextFieldValue,
    confirmPasswordOnValueChange: (TextFieldValue) -> Unit,
    confirmPasswordExceptionMessage: String,
    confirmPasswordVisibility: Boolean,
    confirmPasswordIconOnClick: () -> Unit,

    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)) {


    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {


        EmailTextField(
            email = email,
            emailExceptionMessage = emailExceptionMessage,
            clearIconOnClick = emailClearIconOnClick,
            onValueChange = emailOnValueChange)


        PasswordTextField(
            password = password,
            passwordExceptionMessage = passwordExceptionMessage,
            clearIconOnClick = passwordClearIconOnClick,
            onValueChange = passwordOnValueChange)

        ConfirmPasswordTextField(
            passwordValue = confirmPassword,
            passwordExceptionMessage = confirmPasswordExceptionMessage,
            onValueChange = confirmPasswordOnValueChange,
            keyboardActionOnDone = keyboardActionOnDone,
            passwordVisibility = confirmPasswordVisibility,
            passwordVisibilityIconOnClick = confirmPasswordIconOnClick)

    }
}
