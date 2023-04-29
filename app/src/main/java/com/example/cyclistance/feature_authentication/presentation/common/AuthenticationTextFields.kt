
package com.example.cyclistance.feature_authentication.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cyclistance.theme.Black500


@Composable
fun ConfirmPasswordTextField(
    enabled: Boolean,
    password: String,
    passwordErrorMessage: String,
    isPasswordVisible: Boolean,
    passwordVisibilityIconOnClick: () -> Unit,
    onValueChange: (String) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)) {



    SetupTextField(
        enabled = enabled,
        textFieldValue = password,
        exceptionMessage = passwordErrorMessage,
        onValueChange = onValueChange,
        placeholderText = "Confirm Password",
        trailingIcon = {
            val image =
                if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = passwordVisibilityIconOnClick) {
                Icon(imageVector = image, "", tint = Black500)
            }
        },
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false, imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = keyboardActionOnDone)

    )
}

@Composable
fun PasswordTextField(
    enabled: Boolean,
    password: String,
    passwordExceptionMessage: String,
    clearIconOnClick: () -> Unit,
    onValueChange: (String) -> Unit) {

    val hasError = passwordExceptionMessage.isNotEmpty()


    SetupTextField(
        enabled = enabled,
        textFieldValue = password,
        exceptionMessage = passwordExceptionMessage,
        onValueChange = onValueChange,
        placeholderText = "Password",
        trailingIcon = {
            if (hasError) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "error",
                    tint = MaterialTheme.colors.error)
            }

            if(password.isNotEmpty() && !hasError){
                IconButton(onClick = clearIconOnClick) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "",
                        tint = Black500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false, imeAction = ImeAction.Next),
        visualTransformation = PasswordVisualTransformation()
    )
}






@Composable
private fun SetupTextField(
    enabled: Boolean,
    focusRequester: FocusRequester = FocusRequester(),
    textFieldValue: String,
    exceptionMessage: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: ImageVector = Icons.Default.Lock,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions:KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    val hasError = exceptionMessage.isNotEmpty()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(10.dp, shape = RoundedCornerShape(12.dp), clip = true)
                .focusRequester(focusRequester),
            value = textFieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    text = placeholderText,
                    color = if (hasError) MaterialTheme.colors.error else Black500,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = trailingIcon,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Password Icon",
                    tint = if(hasError) MaterialTheme.colors.error else Black500,
                    modifier = Modifier.size(18.dp)
                )
            },
            isError = hasError,
            keyboardOptions = keyboardOptions,
            colors = textFieldColors(),
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions

        )
        if (hasError) {

            Text(
                text = exceptionMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                )
        }
    }
}


@Composable
fun EmailTextField(
    enabled: Boolean,
    focusRequester: FocusRequester,
    email: String,
    emailErrorMessage: String,
    clearIconOnClick: ()-> Unit,
    onValueChange: (String) -> Unit) {

    val hasError = emailErrorMessage.isNotEmpty()

    SetupTextField(
        enabled = enabled,
        focusRequester = focusRequester,
        textFieldValue = email,
        exceptionMessage = emailErrorMessage,
        onValueChange = onValueChange,
        placeholderText = "Email",
        leadingIcon = Icons.Default.Email,
        trailingIcon = {
            if(hasError){
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "",
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(20.dp)
                )
            }

            if(email.isNotEmpty() && !hasError){
                IconButton(onClick = clearIconOnClick) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "",
                        tint = Black500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
    )
}


@Composable
 fun ErrorMessage(errorMessage: String, modifier: Modifier = Modifier){

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Error,
            tint = MaterialTheme.colors.error,
            modifier = Modifier.size(12.dp),
            contentDescription = "Icon error")
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,

        )
    }
}



