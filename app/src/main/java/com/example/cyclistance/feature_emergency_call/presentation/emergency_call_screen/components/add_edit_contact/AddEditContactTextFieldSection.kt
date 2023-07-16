package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact

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
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldItem

@Composable
fun AddEditContextTextFieldSection(
    modifier: Modifier,
    uiState: EmergencyCallUIState,
    state: EmergencyCallState,
    name: TextFieldValue,
    phoneNumber: TextFieldValue,
    keyboardActions: KeyboardActions,
    event: (EmergencyCallUiEvent) -> Unit) {


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
                onValueChange = { event(EmergencyCallUiEvent.OnChangeName(it)) })
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
                onValueChange = { event(EmergencyCallUiEvent.OnChangePhoneNumber(it)) })

        }
    }
}
