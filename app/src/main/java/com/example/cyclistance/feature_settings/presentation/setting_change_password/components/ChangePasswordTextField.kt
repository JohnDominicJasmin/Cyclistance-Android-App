package com.example.cyclistance.feature_settings.presentation.setting_change_password.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.Black500


@Composable
fun PasswordTextFieldArea(modifier: Modifier) {
    var passwordDummy by remember { mutableStateOf(TextFieldValue()) }
    var passwordVisibility by remember { mutableStateOf(false) }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {


        PasswordTextFieldItem(
            label = "Current Password",
            errorMessage = "",
            value = passwordDummy,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            isPasswordVisible = passwordVisibility,
            togglePasswordVisibilityOnClick = {
                passwordVisibility = !passwordVisibility
            }, onValueChange = {
                passwordDummy = it
            })


        PasswordTextFieldItem(
            label = "New Password",
            errorMessage = "",
            value = passwordDummy,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            isPasswordVisible = passwordVisibility,
            togglePasswordVisibilityOnClick = {
                passwordVisibility = !passwordVisibility
            }, onValueChange = {
                passwordDummy = it
            })



        PasswordTextFieldItem(
            label = "Change Password",
            errorMessage = "",
            value = passwordDummy,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            isPasswordVisible = passwordVisibility,
            togglePasswordVisibilityOnClick = {
                passwordVisibility = !passwordVisibility
            }, onValueChange = {
                passwordDummy = it
            })
    }
}

@Composable
fun PasswordTextFieldItem(
    label: String,
    errorMessage: String,
    value: TextFieldValue,
    isPasswordVisible: Boolean,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    togglePasswordVisibilityOnClick: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
) {

    val hasError = errorMessage.isNotEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Black500,
            modifier = Modifier.padding(bottom = 5.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            BasicTextField(
                modifier = Modifier
                    .weight(weight = 0.9f),
                value = value,
                singleLine = true,
                maxLines = 1,
                onValueChange = onValueChange,
                textStyle = TextStyle(
                    color = MaterialTheme.colors.onBackground,
                ),
                cursorBrush = Brush.verticalGradient(
                    0.00f to MaterialTheme.colors.onBackground,
                    1.00f to MaterialTheme.colors.onBackground),
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),


            )

            val image =
                if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

                Icon(
                    imageVector = image,
                    "",
                    tint = Black500,
                    modifier = Modifier.size(19.dp).clickable { togglePasswordVisibilityOnClick() }
                )


        }

        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = Black450)

        if (hasError) {
           ErrorMessage(errorMessage = errorMessage, modifier = Modifier.padding(1.2.dp))
        }

    }
}

