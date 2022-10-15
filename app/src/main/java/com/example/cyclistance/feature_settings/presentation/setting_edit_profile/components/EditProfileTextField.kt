package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileState
import com.example.cyclistance.theme.*


@Composable
fun TextFieldInputArea(
    modifier: Modifier,
    state: EditProfileState,
    onClickPhoneTextField: () -> Unit,
    onValueChangeName: (String) -> Unit,
    onValueChangePhoneNumber: (String) -> Unit,
    keyboardActions: KeyboardActions
) {


    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(15.dp)) {


        TextFieldItem(
            label = "Full Name",
            enabled = !state.isLoading,
            errorMessage = state.nameErrorMessage,
            value = state.name,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next),
            onValueChange = onValueChangeName)


        TextFieldItem(
            label = "Phone Number",
            errorMessage = state.phoneNumberErrorMessage,
            value = state.phoneNumber,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done),
            keyboardActions = keyboardActions,
            onClick = {
                if (!state.isLoading) {
                    onClickPhoneTextField()
                }
            },
            onValueChange = onValueChangePhoneNumber,
            enabled = false)
    }
}

@Composable
private fun TextFieldItem(
    label: String,
    errorMessage: String,
    value: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit = {}) {

    val hasError = errorMessage.isNotEmpty()

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Black500,
            modifier = Modifier.padding(bottom = 5.dp))

        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            enabled = enabled,
            value = value,
            singleLine = true,
            maxLines = 1,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = MaterialTheme.colors.onBackground
            ),
            cursorBrush = Brush.verticalGradient(
                0.00f to MaterialTheme.colors.onBackground,
                1.00f to MaterialTheme.colors.onBackground),
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = Black450)

        if (hasError) {
            ErrorMessage(errorMessage = errorMessage, modifier = Modifier.padding(1.2.dp))
        }

    }
}

