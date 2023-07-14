package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
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
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.feature_dialogs.domain.model.AlertDialogState
import com.example.cyclistance.feature_dialogs.presentation.alert_dialog.AlertDialog
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact.AddEditContactContent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmergencyCallScreenContent(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: ModalBottomSheetState,
    keyboardActions: KeyboardActions = KeyboardActions { },
    uiState: EmergencyCallUIState,
    state: EmergencyCallState,
    photoUrl: Any?,
    event: (EmergencyCallUiEvent) -> Unit) {

    val contactsAvailable =
        remember(state.emergencyCallModel) { state.emergencyCallModel.contacts.isNotEmpty() }
    val shouldShowAddEditContactDialog =
        remember(uiState.contactCurrentlyEditing) { uiState.contactCurrentlyEditing != null }

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

            if (shouldShowAddEditContactDialog) {
                AddEditContactContent(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    keyboardActions = keyboardActions,
                    event = event,
                    state = state,
                    photoUrl = photoUrl,
                    uiState = uiState
                )
            }



            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally) {

                item {
                    ButtonAddContact(
                        onClick = { event(EmergencyCallUiEvent.OnClickAddContact) },
                        modifier = Modifier.padding(vertical = 16.dp))
                }
                items(items = state.emergencyCallModel.contacts, key = { it.id }) { item ->
                    if (contactsAvailable) {
                        ContactItem(
                            onClick = { event(EmergencyCallUiEvent.OnClickContact(item.phoneNumber)) },
                            emergencyContact = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.dp),
                            onClickEdit = {
                                event(
                                    EmergencyCallUiEvent.OnClickEditContact(
                                        item))
                            },
                            onClickDelete = {
                                event(EmergencyCallUiEvent.OnClickDeleteContact(item))
                            })
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
            photo = EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO,
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

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewEmergencyCallScreenContentDark() {


    val uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }
    CyclistanceTheme(darkTheme = true) {
        EmergencyCallScreenContent(
            uiState = uiState,
            state = EmergencyCallState(
                emergencyCallModel = EmergencyCallModel()
            ),
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Expanded),
            photoUrl = EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO,
            event = {})
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewEmergencyCallScreenContentLight() {

    val uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }

    CyclistanceTheme(darkTheme = false) {
        EmergencyCallScreenContent(
            uiState = uiState,
            state = EmergencyCallState(
                emergencyCallModel = fakeContacts
            ),
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Expanded),
            photoUrl = EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO, event = {})
    }
}


