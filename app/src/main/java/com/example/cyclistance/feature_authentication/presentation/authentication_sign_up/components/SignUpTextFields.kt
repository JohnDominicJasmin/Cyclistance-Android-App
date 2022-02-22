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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.TextFieldColors
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldColor

@Composable
fun SignUpTextFields() {


    val name = remember { mutableStateOf(TextFieldValue("")) }
    val email = remember { mutableStateOf(TextFieldValue("")) }
    val password = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPassword = remember { mutableStateOf(TextFieldValue("")) }


    Column(
        modifier = Modifier
            .layoutId(AuthenticationConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {




        NameTextField(name)

        Spacer(modifier = Modifier.height(10.dp)) // todo: for each spacer if has an error then set height to 0.dp else 13.dp, for now its 13.dp

        EmailTextField(email)

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(password)

        Spacer(modifier = Modifier.height(10.dp))

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
                color = TextFieldColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldColors(),
        trailingIcon = {
            val image =
                if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                Icon(imageVector = image, "", tint = TextFieldColor)
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
                color = TextFieldColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldColors(),
    )

}
@Composable
fun EmailTextField(email:MutableState<TextFieldValue>) {


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
                color = TextFieldColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email Icon",
                tint = TextFieldColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldColors(),
    )

}
@Composable
private fun NameTextField(name: MutableState<TextFieldValue>) {

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .shadow(elevation = 12.dp, shape = RoundedCornerShape(12.dp), clip = true),
        value = name.value,
        onValueChange = { name.value = it },
        singleLine = true,
        shape = RoundedCornerShape(12.dp), label = {
            Text(
                text = "Name",//todo when textfield has error then set value color to red
                color = TextFieldColor,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )

        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Email Icon",
                tint = TextFieldColor
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldColors()
    )
}



