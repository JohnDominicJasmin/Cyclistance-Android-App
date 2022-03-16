package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import java.lang.Error

@Composable
fun SignInTextFieldsSection(
    email: MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    signInState: SignInState<Boolean>) {

    val emailExceptionMessage = signInState.emailExceptionMessage
    val passwordExceptionMessage = signInState.passwordExceptionMessage

    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {


        EmailTextField(email = email )

        if(emailExceptionMessage.isNotEmpty()){
            Text(
                textAlign = TextAlign.Center,
                text = emailExceptionMessage,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
                )
        }
        ConfirmPasswordTextField(password)
        if(passwordExceptionMessage.isNotEmpty()){
            Text(
                textAlign = TextAlign.Center,
                text = emailExceptionMessage,
                style = MaterialTheme.typography.caption.copy(color = MaterialTheme.colors.error)
            )
        }


    }

}

