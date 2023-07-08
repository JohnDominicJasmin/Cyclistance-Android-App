package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyCallModel
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.EmergencyCallScreenContent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.navigation.Screens

@Composable
fun EmergencyCallScreen(navController: NavController, paddingValues: PaddingValues) {

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

    EmergencyCallScreenContent(
        uiState = uiState,
        modifier = Modifier.padding(paddingValues),
        emergencyCallModel = EmergencyCallModel(),
        event = { event ->
            when (event) {
                is EmergencyCallUiEvent.OnClickContact -> {}
                is EmergencyCallUiEvent.OnClickAddContact -> {
                    navController.navigate(Screens.EmergencyCall.AddNewContact.screenRoute)
                }

                is EmergencyCallUiEvent.OnClickCancel -> {
                    navController.popBackStack()
                }

                is EmergencyCallUiEvent.OnClickDeleteContact -> showDeleteDialog(event.emergencyContact)
                is EmergencyCallUiEvent.OnClickEditContact -> {}
                is EmergencyCallUiEvent.DismissDeleteContactDialog -> dismissDeleteDialog()
                is EmergencyCallUiEvent.DeleteContact -> {}

            }
        })
}