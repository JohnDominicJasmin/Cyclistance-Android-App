package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_authentication.presentation.common.ErrorMessage
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileState
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileUiState
import com.example.cyclistance.theme.Black450
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun TextFieldInputArea(
    modifier: Modifier,
    state: EditProfileState,
    uiState: EditProfileUiState,
    onValueChangeName: (String) -> Unit,
    onValueChangePhoneNumber: (String) -> Unit,
    keyboardActions: KeyboardActions
) {


    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {


        TextFieldCreator(label = "Full Name", errorMessage = uiState.nameErrorMessage) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = uiState.name,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                onValueChange = onValueChangeName)

        }


        TextFieldCreator(label = "Phone Number", errorMessage = uiState.phoneNumberErrorMessage, ) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = "+63 | ",
                    color = Black500,
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.subtitle2.fontSize
                )

                TextFieldItem(
                    enabled = !state.isLoading,
                    value = uiState.phoneNumber,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Done),
                    keyboardActions = keyboardActions,
                    onValueChange = onValueChangePhoneNumber)

            }
        }

    }
}

@Composable
fun TextFieldCreator(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    label: String,
    content: @Composable () -> Unit) {
    val hasError by remember(errorMessage) { derivedStateOf { errorMessage.isNotEmpty() } }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {

        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Black500,
            modifier = Modifier.padding(bottom = 5.dp))

        content()

        Divider(
            modifier = Modifier
                .fillMaxWidth(), color = Black450)

        if (hasError) {
            ErrorMessage(
                errorMessage = errorMessage,
                modifier = Modifier.padding(1.2.dp))
        }

    }
}

@Composable
fun TextFieldItem(
    value: String,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions = KeyboardActions(),
    onValueChange: (String) -> Unit,
    enabled: Boolean = true,
    onClick: () -> Unit = {}) {


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
            color = MaterialTheme.colors.onBackground,
            fontSize = MaterialTheme.typography.subtitle2.fontSize,
        ),
        cursorBrush = Brush.verticalGradient(
            0.00f to MaterialTheme.colors.onBackground,
            1.00f to MaterialTheme.colors.onBackground),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions
    )


}

@Preview
@Composable
fun EditProfileTextFieldPreview() {
    CyclistanceTheme(darkTheme = true) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {
            TextFieldInputArea(
                modifier = Modifier, state = EditProfileState(),
                onValueChangeName = { },
                onValueChangePhoneNumber = {},
                keyboardActions = KeyboardActions { },
                uiState = EditProfileUiState(),
            )
        }
    }

}