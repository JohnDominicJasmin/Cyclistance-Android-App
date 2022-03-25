
package com.example.cyclistance.feature_authentication.presentation.common

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
import com.example.cyclistance.feature_authentication.presentation.theme.TextFieldTextColor

@Composable
fun SignUpTextFieldsSection(
    email: MutableState<TextFieldValue>,
    emailOnValueChange: (TextFieldValue) -> Unit,
    name: MutableState<TextFieldValue>,
    nameOnValueChange: (TextFieldValue) -> Unit,
    password: MutableState<TextFieldValue>,
    passwordOnValueChange: (TextFieldValue) -> Unit,
    confirmPassword: MutableState<TextFieldValue>,
    confirmPasswordOnValueChange: (TextFieldValue) -> Unit,
    inputResultState: InputResultState<Boolean>) {

    val emailExceptionMessage = inputResultState.emailExceptionMessage
    val passwordExceptionMessage = inputResultState.passwordExceptionMessage
    val confirmPasswordExceptionMessage = inputResultState.confirmPasswordExceptionMessage

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

        NameTextField(
            name = name,
            nameExceptionMessage = "",
            onValueChange = nameOnValueChange
        )

        PasswordTextField(
            password = password,
            passwordExceptionMessage = passwordExceptionMessage,
            onValueChange = passwordOnValueChange)

        ConfirmPasswordTextField(
            confirmPassword = confirmPassword,
            confirmPasswordExceptionMessage = confirmPasswordExceptionMessage,
            onValueChange = confirmPasswordOnValueChange)

    }
}




@Composable
fun ConfirmPasswordTextField(
    confirmPassword: MutableState<TextFieldValue>,
    confirmPasswordExceptionMessage: String,
    onValueChange: (TextFieldValue) -> Unit) {

    var confirmPasswordVisibility by remember { mutableStateOf(false) }
    val hasError = confirmPasswordExceptionMessage.isNotEmpty()


    SetupPasswordTextField(
        password = confirmPassword,
        passwordExceptionMessage = confirmPasswordExceptionMessage,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Confirm Password",
                color = if (hasError) MaterialTheme.colors.error else TextFieldTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }, leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldTextColor,
                modifier = Modifier.size(18.dp)

            )
        },trailingIcon = {
            val image =
                if (confirmPasswordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { confirmPasswordVisibility = !confirmPasswordVisibility }) {
                Icon(imageVector = image, "", tint = TextFieldTextColor)
            }
        },
        visualTransformation = if (confirmPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, autoCorrect = false, imeAction = ImeAction.Done)
    )
}

@Composable
fun PasswordTextField(
    password: MutableState<TextFieldValue>,
    passwordExceptionMessage: String,
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = passwordExceptionMessage.isNotEmpty()


    SetupPasswordTextField(
        password = password,
        passwordExceptionMessage = passwordExceptionMessage,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = "Password",
                color = if (hasError) MaterialTheme.colors.error else TextFieldTextColor,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Password Icon",
                tint = TextFieldTextColor,
                modifier = Modifier.size(18.dp)
            )
        },
        trailingIcon = {
            if (hasError) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "error",
                    tint = MaterialTheme.colors.error)
            }

            if(password.value.text.isNotEmpty()){
                IconButton(onClick = {
                    password.value = TextFieldValue("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "",
                        tint = TextFieldTextColor,
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
private fun SetupPasswordTextField(
    password: MutableState<TextFieldValue>,
    passwordExceptionMessage: String,
    onValueChange: (TextFieldValue) -> Unit,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions:KeyboardOptions
) {
    val hasError = passwordExceptionMessage.isNotEmpty()
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
            value = password.value,
            onValueChange = onValueChange,
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            leadingIcon = leadingIcon,
            isError = hasError,
            keyboardOptions = keyboardOptions,
            colors = TextFieldColors(),
            visualTransformation = visualTransformation
        )
        if (hasError) {
            Text(
                text = passwordExceptionMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}



@Composable
fun NameTextField(
    name: MutableState<TextFieldValue>,
    nameExceptionMessage:String,
    onValueChange: (TextFieldValue) -> Unit) {


    val hasError = nameExceptionMessage.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
            value = name.value,
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Email",
                    color = if (hasError) MaterialTheme.colors.error else TextFieldTextColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {


                if (hasError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (name.value.text.isNotEmpty()) {
                    IconButton(onClick = {
                        name.value = TextFieldValue("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = TextFieldTextColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }


            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Icon",
                    tint = TextFieldTextColor,
                    modifier = Modifier.size(18.dp)
                )
            },
            isError = hasError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next),
            colors = TextFieldColors(),
        )

        if (hasError) {

            Text(
                text = nameExceptionMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )


        }
    }



}

@Composable
fun EmailTextField(
    emailExceptionMessage: String,
    email: MutableState<TextFieldValue>,
    onValueChange: (TextFieldValue) -> Unit) {

    val hasError = emailExceptionMessage.isNotEmpty()

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(3.dp)) {

        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .shadow(15.dp, shape = RoundedCornerShape(12.dp), clip = true),
            value = email.value,
            singleLine = true,
            maxLines = 1,
            shape = RoundedCornerShape(12.dp),
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = "Name",
                    color = if (hasError) MaterialTheme.colors.error else TextFieldTextColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            },
            trailingIcon = {


                if(hasError){
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if(email.value.text.isNotEmpty()){
                    IconButton(onClick = {
                        email.value = TextFieldValue("")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Cancel,
                            contentDescription = "",
                            tint = TextFieldTextColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }



            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Email Icon",
                    tint = TextFieldTextColor,
                    modifier = Modifier.size(18.dp)
                )
            },
            isError = hasError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
            colors = TextFieldColors(),
        )

        if (hasError) {

            Text(
                text = emailExceptionMessage,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 16.dp)
            )


        }
    }

}



