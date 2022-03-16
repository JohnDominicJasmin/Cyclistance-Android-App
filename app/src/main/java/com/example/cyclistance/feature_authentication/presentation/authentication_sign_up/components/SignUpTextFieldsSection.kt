package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.SignInState
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.SignUpState
import com.example.cyclistance.feature_authentication.presentation.common.TextFieldColors
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldTextColor

@Composable
fun SignUpTextFieldsSection(
    email : MutableState<TextFieldValue>,
    password: MutableState<TextFieldValue>,
    confirmPassword: MutableState<TextFieldValue>,
    signUpState: SignUpState<Boolean>
) {




    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(13.dp)) {




       // todo: for each spacer if has an error then set height to 0.dp else 13.dp, for now its 13.dp

        EmailTextField(email)
        PasswordTextField(password)
        ConfirmPasswordTextField(confirmPassword)

    }
}

@Composable
fun ConfirmPasswordTextField(confirmPassword:MutableState<TextFieldValue>) {
    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
        value = confirmPassword.value,
        onValueChange = { confirmPassword.value = it },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                text = "Confirm Password",
                color = TextFieldTextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldColors(),
        trailingIcon = {
            val image =
                if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                Icon(imageVector = image, "", tint = TextFieldTextColor)
            }
        },
        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
    )

}
@Composable
fun PasswordTextField(password:MutableState<TextFieldValue>) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
        value = password.value,
        onValueChange = { password.value = it },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        label = {
            Text(
                text = "Password",
                color = TextFieldTextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldColors(),
    )

}
@Composable
fun EmailTextField(email:MutableState<TextFieldValue>) {

    //todo change color when errors are displayed
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
        value = email.value,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        onValueChange = { email.value = it },
        label = {
            Text(
                text = "Email",
                color = TextFieldTextColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon",
                tint = TextFieldTextColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldColors(),
    )

}


