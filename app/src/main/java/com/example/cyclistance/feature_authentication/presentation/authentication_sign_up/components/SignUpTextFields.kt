package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
    emailClearIconOnClick: () -> Unit,

    name: TextFieldValue,
    nameOnValueChange: (TextFieldValue) -> Unit,
    nameClearIconOnClick: () -> Unit,

    password: TextFieldValue,
    passwordOnValueChange: (TextFieldValue) -> Unit,
    passwordClearIconOnClick: () -> Unit,

    confirmPassword: TextFieldValue,
    confirmPasswordOnValueChange: (TextFieldValue) -> Unit,

    inputResultState: AuthState<Boolean>,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)) {

    val emailExceptionMessage = inputResultState.emailExceptionMessage
    val passwordExceptionMessage = inputResultState.passwordExceptionMessage
    val confirmPasswordExceptionMessage = inputResultState.confirmPasswordExceptionMessage

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

        NameTextField(
            name = name,
            nameExceptionMessage = "",
            clearIconOnClick = nameClearIconOnClick,
            onValueChange = nameOnValueChange
        )

        PasswordTextField(
            password = password,
            passwordExceptionMessage = passwordExceptionMessage,
            clearIconOnClick = passwordClearIconOnClick,
            onValueChange = passwordOnValueChange)

        ConfirmPasswordTextField(
            confirmPassword = confirmPassword,
            confirmPasswordExceptionMessage = confirmPasswordExceptionMessage,
            onValueChange = confirmPasswordOnValueChange,
            keyboardActionOnDone = keyboardActionOnDone)

    }
}
