package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditContactUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactUiState
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldItem
import com.example.cyclistance.theme.Black500

@Composable
fun AddEditContextTextFieldSection(
    modifier: Modifier,
    uiState: AddEditContactUiState,
    state: AddEditContactState,
    keyboardActions: KeyboardActions,
    event: (AddEditContactUiEvent) -> Unit) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)) {

        TextFieldCreator(label = "Name", errorMessage = uiState.nameErrorMessage) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = uiState.name,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                onValueChange = { event(AddEditContactUiEvent.OnChangeName(it)) })
        }

        TextFieldCreator(label = "Phone", errorMessage = "") {
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
                    onValueChange = { event(AddEditContactUiEvent.OnChangePhoneNumber(it)) })

            }
        }
    }
}
