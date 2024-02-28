package com.myapp.cyclistance.feature_authentication.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.theme.Black500


@Composable
fun ConfirmPasswordTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    password: TextFieldValue,
    passwordErrorMessage: String,
    isPasswordVisible: Boolean,
    passwordVisibilityIconOnClick: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardActionOnDone: (KeyboardActionScope.() -> Unit)) {


    SetupTextField(
        modifier = modifier,
        enabled = enabled,
        textFieldValue = password,
        failureMessage = passwordErrorMessage,
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
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
            imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = keyboardActionOnDone)

    )
}

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    placeholderText: String = "Password",
    enabled: Boolean,
    password: TextFieldValue,
    hasTrailingIcon: Boolean = true,
    passwordExceptionMessage: String,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Next),
    clearIconOnClick: () -> Unit = {},
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = passwordExceptionMessage.isNotEmpty()


    SetupTextField(
        modifier = modifier,
        enabled = enabled,
        textFieldValue = password,
        failureMessage = passwordExceptionMessage,
        onValueChange = onValueChange,
        placeholderText = placeholderText,
        keyboardActions = keyboardActions,
        trailingIcon = {

            if(hasTrailingIcon) {

                AnimatedVisibility(
                    visible = hasError,
                    enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = MaterialTheme.colors.error)
                }

                AnimatedVisibility(
                    visible = password.text.isNotEmpty() && !hasError,
                    enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                    IconButton(onClick = clearIconOnClick) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = Black500,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

            }
        },
        keyboardOptions = keyboardOptions,
        visualTransformation = PasswordVisualTransformation()
    )
}


@Composable
private fun SetupTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    textFieldValue: TextFieldValue,
    failureMessage: String,
    onValueChange: (TextFieldValue) -> Unit,
    placeholderText: String,
    leadingIcon: ImageVector = Icons.Default.Lock,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    val hasError = failureMessage.isNotEmpty()

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp), clip = true)
                .clearAndSetSemantics { },
            value = textFieldValue,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            placeholder = {
                Text(
                    modifier = Modifier.padding(horizontal = 3.dp),
                    text = placeholderText,
                    style = MaterialTheme.typography.subtitle2.copy(
                        fontWeight = FontWeight.Normal,
                        color = if (hasError) MaterialTheme.colors.error else Black500,
                        textAlign = TextAlign.Center),
                )
            },
            trailingIcon = trailingIcon,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = "Password Icon",
                    tint = if (hasError) MaterialTheme.colors.error else Black500,
                    modifier = Modifier.size(18.dp)
                )
            },
            isError = hasError,
            keyboardOptions = keyboardOptions,
            colors = textFieldColors(),
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            textStyle = TextStyle(
                fontSize = TextUnit(value = 14f, type = TextUnitType.Sp)
            )


        )

        AnimatedVisibility(
            visible = hasError,
            enter = fadeIn(animationSpec = tween(durationMillis = 100)),
            exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

            Text(
                text = failureMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}


@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    email: TextFieldValue,
    keyboardActions: KeyboardActions = KeyboardActions(),
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Email,
        imeAction = ImeAction.Next),
    emailErrorMessage: String,
    clearIconOnClick: () -> Unit,
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = emailErrorMessage.isNotEmpty()

    SetupTextField(
        keyboardActions = keyboardActions,
        modifier = modifier,
        enabled = enabled,
        textFieldValue = email,
        failureMessage = emailErrorMessage,
        onValueChange = onValueChange,
        placeholderText = "Email",
        leadingIcon = Icons.Default.Email,
        trailingIcon = {

            AnimatedVisibility(
                visible = hasError,
                enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "Error Icon",
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(
                visible = email.text.isNotEmpty() && !hasError,
                enter = fadeIn(animationSpec = tween(durationMillis = 100)),
                exit = fadeOut(animationSpec = tween(durationMillis = 100))) {

                IconButton(onClick = clearIconOnClick) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Clear Icon",
                        tint = Black500,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        },
        keyboardOptions = keyboardOptions,
    )
}


@Composable
fun ErrorMessage(errorMessage: String, modifier: Modifier = Modifier) {

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



