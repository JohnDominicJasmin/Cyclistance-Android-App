package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.ConfirmPasswordTextField
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components.EmailTextField
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

@Composable
fun SignInTextFieldsArea() {


    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }


    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {


        EmailTextField(email)

        /*

        FOR VALIDATION

        Text(
            textAlign = TextAlign.Center,
            text = if (true) "Requires '@' and at least 5 symbols" else "Helper message",
            style = MaterialTheme.typography.caption.copy(color = if(true) MaterialTheme.colors.error else MaterialTheme.colors.onSurface

            ),
        )
        */
        // todo: if has an error then set height to 0.dp else 13.dp, for now its 13.dp
        ConfirmPasswordTextField(password)

    }

}

