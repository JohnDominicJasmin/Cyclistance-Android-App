package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.ui.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.theme.Black500
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun EmergencyCallScreenContent(
    modifier: Modifier = Modifier,
    emergencyCallModel: EmergencyCallModel,
    uiState: EmergencyCallUIState,
    event: (EmergencyCallUiEvent) -> Unit) {

    val messageAvailable by remember(emergencyCallModel.contacts) { derivedStateOf { emergencyCallModel.contacts.isNotEmpty() } }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {


        Box(modifier = Modifier) {

            if (uiState.deleteDialogVisible) {
                DeleteContactDialog(
                    onDismissRequest = { event(EmergencyCallUiEvent.DismissDeleteContactDialog) },
                    onClickConfirmButton = { event(EmergencyCallUiEvent.DeleteContact(uiState.contactToDelete.id)) },
                    nameToDelete = uiState.contactToDelete.name
                )
            }

            Column(verticalArrangement = Arrangement.SpaceEvenly) {


                ButtonAddContact(
                    onClick = { event(EmergencyCallUiEvent.OnClickAddContact) },
                    modifier = Modifier.padding(vertical = 16.dp))


                if (messageAvailable) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f),
                        horizontalAlignment = Alignment.CenterHorizontally) {


                        items(items = emergencyCallModel.contacts, key = { it.id }) { item ->
                            ContactItem(
                                onClick = { event(EmergencyCallUiEvent.OnClickContact) },
                                emergencyContact = item,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 1.dp),
                                onClickEdit = { event(EmergencyCallUiEvent.OnClickEditContact(it)) },
                                onClickDelete = { event(EmergencyCallUiEvent.OnClickDeleteContact(it)) })
                        }
                    }

                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f), contentAlignment = Alignment.Center) {
                        Text(
                            text = "No contacts added",
                            style = MaterialTheme.typography.h6,
                            color = Black500
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.2f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center) {

                    OutlinedButton(
                        modifier = Modifier.wrapContentSize(),
                        onClick = { event(EmergencyCallUiEvent.OnClickCancel) },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                        border = BorderStroke(1.dp, Black500), shape = RoundedCornerShape(12.dp)) {

                        Text(
                            text = "Close",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.button,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 2.dp))
                    }

                }
            }
        }

    }
}


private val fakeContacts = EmergencyCallModel(
    contacts = listOf(
        EmergencyContactModel(
            id = "1",
            name = EmergencyCallConstants.PHILIPPINE_RED_CROSS,
            photo = EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO,
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "12323",
            name = EmergencyCallConstants.NATIONAL_EMERGENCY,
            photo = EmergencyCallConstants.NATIONAL_EMERGENCY_PHOTO,
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "2",
            name = "John Doe",
            photo = "",
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "3",
            name = "John Doe",
            photo = "",
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "4",
            name = "John Doe",
            photo = "",
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "5",
            name = "John Doe",
            photo = "",
            number = "123456789"
        ),
        EmergencyContactModel(
            id = "52",
            name = "John Doe",
            photo = "",
            number = "123456789"
        ),

        ))

@Preview
@Composable
fun PreviewEmergencyCallScreenContentDark() {


    var uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }

    val showDeleteDialog = remember {
        { emergencyContact: EmergencyContactModel ->
            uiState = uiState.copy(deleteDialogVisible = true, contactToDelete = emergencyContact)
        }
    }

    val dismissDeleteDialog = remember {
        {
            uiState =
                uiState.copy(deleteDialogVisible = false, contactToDelete = EmergencyContactModel())
        }
    }

    CyclistanceTheme(darkTheme = true) {
        EmergencyCallScreenContent(
            uiState = uiState,
            emergencyCallModel = fakeContacts, event = { event ->
                when (event) {
                    is EmergencyCallUiEvent.OnClickContact -> {

                    }

                    is EmergencyCallUiEvent.OnClickAddContact -> {
                    }

                    is EmergencyCallUiEvent.OnClickCancel -> {
                    }

                    is EmergencyCallUiEvent.OnClickDeleteContact -> showDeleteDialog(event.emergencyContact)
                    is EmergencyCallUiEvent.OnClickEditContact -> {

                    }

                    is EmergencyCallUiEvent.DismissDeleteContactDialog -> dismissDeleteDialog()
                    is EmergencyCallUiEvent.DeleteContact -> {}

                }
            })
    }
}

@Preview
@Composable
fun PreviewEmergencyCallScreenContentLight() {

    var uiState by rememberSaveable {
        mutableStateOf(EmergencyCallUIState())
    }

    val showDeleteDialog = remember {
        { emergencyContact: EmergencyContactModel ->
            uiState = uiState.copy(deleteDialogVisible = true, contactToDelete = emergencyContact)
        }
    }

    val dismissDeleteDialog = remember {
        {
            uiState =
                uiState.copy(deleteDialogVisible = false, contactToDelete = EmergencyContactModel())
        }
    }

    CyclistanceTheme(darkTheme = false) {
        EmergencyCallScreenContent(
            uiState = uiState,
            emergencyCallModel = fakeContacts, event = { event ->
                when (event) {
                    is EmergencyCallUiEvent.OnClickContact -> {

                    }

                    is EmergencyCallUiEvent.OnClickAddContact -> {

                    }

                    is EmergencyCallUiEvent.OnClickCancel -> {

                    }

                    is EmergencyCallUiEvent.OnClickDeleteContact -> showDeleteDialog(event.emergencyContact)
                    is EmergencyCallUiEvent.OnClickEditContact -> {

                    }

                    is EmergencyCallUiEvent.DismissDeleteContactDialog -> dismissDeleteDialog()
                    is EmergencyCallUiEvent.DeleteContact -> {}
                }
            })
    }
}


