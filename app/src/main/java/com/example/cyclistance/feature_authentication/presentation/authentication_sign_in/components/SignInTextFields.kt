package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInViewModel
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.common.InputResultState

@Composable
fun SignInTextFieldsSection(
    email: MutableState<TextFieldValue>,
    emailExceptionMessage:String,
    emailOnValueChange: (TextFieldValue) -> Unit,
    password: MutableState<TextFieldValue>,
    passwordExceptionMessage:String,
    passwordOnValueChange: (TextFieldValue) -> Unit
    ) {



    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {



        EmailTextField(
            email = email,
            emailExceptionMessage = emailExceptionMessage,
            onValueChange = emailOnValueChange)

        ConfirmPasswordTextField(
            confirmPassword = password,
            confirmPasswordExceptionMessage = passwordExceptionMessage,
            onValueChange = passwordOnValueChange)



    }

}

