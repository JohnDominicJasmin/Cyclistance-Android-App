package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldColor

@Composable
fun TextFieldsArea() {
    Column(
        modifier = Modifier
            .layoutId(SignInConstraintsItem.TextFields.layoutId)
            .fillMaxWidth(fraction = 0.9f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var passwordVisibility by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
            value = email,
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            onValueChange = { email = it },
            label = {
                Text(
                    text = "Email",
                    color = TextFieldColor,
                    fontSize = 13.sp,
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

        /*
        val textColor = if (true) {
            MaterialTheme.colors.error
        } else {
            MaterialTheme.colors.onSurface
        }
        Text(
            textAlign = TextAlign.Center,
            text = if (true) "Requires '@' and at least 5 symbols" else "Helper message",
            style = MaterialTheme.typography.caption.copy(color = textColor),
        )
         */



        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            label = {
                Text(
                    text = "Password",
                    color = TextFieldColor,
                    fontSize = 13.sp,
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
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, "", tint = TextFieldColor)
                }
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        )

    }

}