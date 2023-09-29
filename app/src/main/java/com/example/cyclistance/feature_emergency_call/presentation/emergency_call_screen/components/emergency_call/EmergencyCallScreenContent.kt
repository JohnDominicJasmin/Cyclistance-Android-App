package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.R
import com.example.cyclistance.core.domain.model.AlertDialogState
import com.example.cyclistance.core.presentation.dialogs.alert_dialog.AlertDialog
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme


@Composable
fun EmergencyCallScreenContent(
    modifier: Modifier = Modifier,
    uiState: EmergencyCallUIState,
    state: EmergencyCallState,
    event: (EmergencyCallUiEvent) -> Unit) {

    val contactsAvailable =
        remember(state.emergencyCallModel) { state.emergencyCallModel.contacts.isNotEmpty() }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        Box {

            if (uiState.maximumContactDialogVisible) {
                AlertDialog(
                    alertDialog = AlertDialogState(
                        title = "Maximum number of contacts reached",
                        description = "You can only add ${EmergencyCallConstants.MAX_CONTACTS} contacts",
                        icon = R.raw.info),
                    onDismissRequest = { event(EmergencyCallUiEvent.DismissMaximumContactDialog) })
            }

            if (uiState.deleteDialogVisible) {
                DeleteContactDialog(
                    onDismissRequest = { event(EmergencyCallUiEvent.DismissDeleteContactDialog) },
                    onClickConfirmButton = {
                        event(EmergencyCallUiEvent.DeleteContact(uiState.contactToDelete))
                        event(EmergencyCallUiEvent.DismissDeleteContactDialog)
                    },
                    nameToDelete = uiState.contactToDelete.name
                )
            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                item {
                    ButtonAddContact(
                        onClick = { event(EmergencyCallUiEvent.OnClickAddContact) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp))
                }
                items(items = state.emergencyCallModel.contacts, key = { it.id }) { item ->
                    if (contactsAvailable) {
                        ContactItem(
                            emergencyContact = item,
                            event = event,
                            uiState = uiState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.dp))

                    }
                }

            }


            if (!contactsAvailable) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No contacts added",
                        style = MaterialTheme.typography.h6,
                        color = Black500
                    )
                }
            }

        }

    }
}


private val fakeContacts = EmergencyCallModel(
    contacts = listOf(
        EmergencyContactModel(
            id = 1,
            name = EmergencyCallConstants.PHILIPPINE_RED_CROSS,
            photo = PHILIPPINE_RED_CROSS_PHOTO,
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 12323,
            name = EmergencyCallConstants.NATIONAL_EMERGENCY,
            photo = EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO,
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 2,
            name = "John Doe",
            photo = "",
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 3,
            name = "John Doe",
            photo = "",
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 4,
            name = "John Doe",
            photo = "",
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 5,
            name = "John Doe",
            photo = "",
            phoneNumber = "123456789"
        ),
        EmergencyContactModel(
            id = 52,
            name = "John Doe",
            photo = "",
            phoneNumber = "123456789"
        ),

        ))

@Preview(name = "Currently editing dark theme")
@Composable
fun PreviewEmergencyCallScreenContent1() {

    val uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState(

        ))
    }
    CyclistanceTheme(darkTheme = true) {
        EmergencyCallScreenContent(
            uiState = uiState.copy(),
            state = EmergencyCallState(
                emergencyCallModel = EmergencyCallModel()
            ),

            event = {})
    }
}

@Preview(name = "Currently editing light theme")
@Composable
fun PreviewEmergencyCallScreenContent2() {

    val uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }

    CyclistanceTheme(darkTheme = false) {
        EmergencyCallScreenContent(
            uiState = uiState.copy(),
            state = EmergencyCallState(
                emergencyCallModel = fakeContacts,
            ),
            event = {})
    }
}


@Preview(name = "Not editing dark theme")
@Composable
fun PreviewEmergencyCallScreenContent3() {


    val uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }
    CyclistanceTheme(darkTheme = true) {
        EmergencyCallScreenContent(
            uiState = uiState.copy(),
            state = EmergencyCallState(
                emergencyCallModel = EmergencyCallModel()
            ),
            event = {} )
    }
}