package com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myapp.cyclistance.core.presentation.text_fields.TextFieldCreator
import com.myapp.cyclistance.core.presentation.text_fields.TextFieldItem
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactUiEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactState
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactUiState

@Composable
fun AddEditContextTextFieldSection(
    modifier: Modifier,
    uiState: AddEditContactUiState,
    state: AddEditContactState,
    name: TextFieldValue,
    phoneNumber: TextFieldValue,
    keyboardActions: KeyboardActions,
    event: (AddEditContactUiEvent) -> Unit) {


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)) {

        TextFieldCreator(label = "Name", errorMessage = uiState.nameErrorMessage) {
            TextFieldItem(
                enabled = !state.isLoading,
                value = name,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next),
                onValueChange = { event(AddEditContactUiEvent.OnChangeName(it)) })
        }

        TextFieldCreator(label = "Phone", errorMessage = uiState.phoneNumberErrorMessage) {


            TextFieldItem(
                enabled = !state.isLoading,
                value = phoneNumber,
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
