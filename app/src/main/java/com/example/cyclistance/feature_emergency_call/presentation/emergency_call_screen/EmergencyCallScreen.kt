package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.MAX_CONTACTS
import com.example.cyclistance.core.utils.contexts.callPhoneNumber
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.emergency_call.EmergencyCallScreenContent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallVmEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.navigation.Screens
import com.example.cyclistance.navigation.nav_graph.navigateScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class,
    ExperimentalComposeUiApi::class)
@Composable
fun EmergencyCallScreen(
    viewModel: EmergencyCallViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {

    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()


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

    val maximumContactReached by remember(state.emergencyCallModel) {
        derivedStateOf {
            state.emergencyCallModel.contacts.size >= MAX_CONTACTS
        }
    }


    val onClickAddContact = remember {
        {

            uiState = uiState.copy(maximumContactDialogVisible = maximumContactReached)

            if (!maximumContactReached) {
                navController.navigateScreen(route = Screens.EmergencyCallNavigation.AddEditEmergencyContact.screenRoute)
            }


        }
    }

    val onClickEditContact = remember {
        { model: EmergencyContactModel ->

            navController.navigateScreen(
                route = Screens.EmergencyCallNavigation.AddEditEmergencyContact.passArgument(
                    contactId = model.id))
        }
    }

    val deleteContact = remember {
        { emergencyContact: EmergencyContactModel ->
            viewModel.onEvent(event = EmergencyCallVmEvent.DeleteContact(emergencyContact))
        }
    }

    val callPhoneNumber = remember {
        { phoneNumber: String ->
            context.callPhoneNumber(phoneNumber)
        }
    }

    val openPhoneCallPermissionState =
        rememberPermissionState(permission = Manifest.permission.CALL_PHONE) { permissionGranted ->
            if (permissionGranted) {
                uiState.selectedPhoneNumber.takeIf { it.isNotEmpty() }
                    ?.let { callPhoneNumber(it) }
            }
        }


    val onClickContact = remember {
        { phoneNumber: String ->
            if (!openPhoneCallPermissionState.status.isGranted) {
                uiState = uiState.copy(selectedPhoneNumber = phoneNumber)
                openPhoneCallPermissionState.launchPermissionRequest()
            } else {
                callPhoneNumber(phoneNumber)
            }

        }
    }

    val dismissMaximumDialog = remember {
        {
            uiState = uiState.copy(maximumContactDialogVisible = false)
        }
    }



    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EmergencyCallEvent.ContactDeleteFailed -> {
                    Toast.makeText(context, "Failed to delete contact", Toast.LENGTH_SHORT).show()
                }

                is EmergencyCallEvent.ContactDeleteSuccess -> {
                    Toast.makeText(context, "Contact deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    EmergencyCallScreenContent(
        uiState = uiState,
        modifier = Modifier.padding(paddingValues),
        state = state,
        event = { event ->
            when (event) {
                is EmergencyCallUiEvent.OnClickContact -> onClickContact(event.phoneNumber)
                is EmergencyCallUiEvent.OnClickAddContact -> onClickAddContact()
                is EmergencyCallUiEvent.OnClickDeleteContact -> showDeleteDialog(event.emergencyContact)
                is EmergencyCallUiEvent.OnClickEditContact -> onClickEditContact(event.emergencyContact)
                is EmergencyCallUiEvent.DismissDeleteContactDialog -> dismissDeleteDialog()
                is EmergencyCallUiEvent.DeleteContact -> deleteContact(event.emergencyContact)
                is EmergencyCallUiEvent.DismissMaximumContactDialog -> dismissMaximumDialog()
            }
        })
}