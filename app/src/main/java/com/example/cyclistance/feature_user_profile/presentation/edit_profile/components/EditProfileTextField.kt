package com.example.cyclistance.feature_user_profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.presentation.text_fields.TextFieldCreator
import com.example.cyclistance.core.presentation.text_fields.TextFieldItem
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.state.EditProfileState
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.state.EditProfileUiState
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun TextFieldInputArea(
    modifier: Modifier,
    state: EditProfileState,
    uiState: EditProfileUiState,
    name: TextFieldValue,
    cyclingGroup: TextFieldValue,
    city: TextFieldValue,
    onValueChangeName: (TextFieldValue) -> Unit,
    onValueChangeCyclingGroup: (TextFieldValue) -> Unit,
    onValueChangeCity: (TextFieldValue) -> Unit,
    keyboardActions: KeyboardActions
) {


    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(20.dp)) {


        TextFieldCreator(label = "Full Name", errorMessage = uiState.nameErrorMessage) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = name,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                onValueChange = onValueChangeName)

        }


        TextFieldCreator(label = "Cycling group", errorMessage = uiState.cyclingGroupErrorMessage, isOptional = true) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = cyclingGroup,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                onValueChange = onValueChangeCyclingGroup)

        }

        TextFieldCreator(label = "City", errorMessage = uiState.addressErrorMessage) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = city,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done),
                onValueChange = onValueChangeCity,
                keyboardActions = keyboardActions)

        }

    }
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
                keyboardActions = KeyboardActions { },
                uiState = EditProfileUiState(),
                name = TextFieldValue(""),
                city = TextFieldValue(""),
                cyclingGroup = TextFieldValue(""),
                onValueChangeCyclingGroup = { },
                onValueChangeCity = { },

            )
        }
    }

}