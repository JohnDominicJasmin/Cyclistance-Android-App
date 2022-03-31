package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActionScope

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.AuthState
import com.example.cyclistance.feature_authentication.presentation.common.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.common.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun SignInTextFieldsArea(
    email: TextFieldValue,
    emailOnValueChange: (TextFieldValue) -> Unit,
    emailExceptionMessage: String,
    emailClearIconOnClick: () -> Unit,
    password: TextFieldValue,
    passwordOnValueChange: (TextFieldValue) -> Unit,
    passwordExceptionMessage: String,
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
            onValueChange = emailOnValueChange,
            clearIconOnClick = emailClearIconOnClick)

        ConfirmPasswordTextField(
            confirmPassword = password,
            confirmPasswordExceptionMessage = passwordExceptionMessage,
            onValueChange = passwordOnValueChange,
            keyboardActionOnDone = keyboardActionOnDone)



    }

}

