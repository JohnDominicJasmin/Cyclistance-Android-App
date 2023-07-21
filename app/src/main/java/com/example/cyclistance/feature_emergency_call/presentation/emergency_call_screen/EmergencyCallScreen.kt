package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
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
    paddingValues: PaddingValues,
    shouldOpenAddNewContact: Boolean) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var phoneNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }


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


    val onCloseEditContactScreen = remember(shouldOpenAddNewContact) {
        {
            if (shouldOpenAddNewContact) {
                navController.popBackStack()
            } else {
                uiState = uiState.copy(contactCurrentlyEditing = null)
            }
        }
    }

    val onClickAddContact = remember {
        {

            if (!maximumContactReached) {
                uiState = uiState.copy(
                    contactCurrentlyEditing = EmergencyContactModel(),
                    nameErrorMessage = "",
                    phoneNumberErrorMessage = ""
                )
                name = name.copy(text = "")
                phoneNumber = phoneNumber.copy(text = "")
            }

            viewModel.onEvent(event = EmergencyCallVmEvent.ResetSnapshot)
            uiState = uiState.copy(maximumContactDialogVisible = maximumContactReached)

        }
    }

    val onClickEditContact = remember {
        { model: EmergencyContactModel ->
            viewModel.onEvent(event = EmergencyCallVmEvent.GetContact(model.id))
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


    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)





    val onValueChangeName = remember {
        { _name: TextFieldValue ->
            uiState = uiState.copy(nameErrorMessage = "")
            name = _name
        }
    }
    val onValueChangePhoneNumber = remember {
        { _phoneNumber: TextFieldValue ->
            uiState = uiState.copy(phoneNumberErrorMessage = "")
            phoneNumber = _phoneNumber
        }
    }

    val saveAddEditContact = remember {
        {
            viewModel.onEvent(
                event = EmergencyCallVmEvent.SaveContact(
                    emergencyContactModel = EmergencyContactModel(
                        id = uiState.contactCurrentlyEditing?.id ?: 0,
                        name = name.text,
                        phoneNumber = phoneNumber.text,
                        photo = uiState.selectedImageUri
                    )
                ))
        }
    }
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            saveAddEditContact()
        })
    }


    val onSaveContactSuccess = remember {
        {
            Toast.makeText(context, "Contact Saved", Toast.LENGTH_LONG).show()
        }
    }

    val onUnknownFailure = remember {
        { message: String ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
    val onPhoneFailure = remember {
        { message: String ->
            uiState = uiState.copy(phoneNumberErrorMessage = message)
        }
    }
    val onNameFailure = remember {
        { message: String ->
            uiState = uiState.copy(nameErrorMessage = message)
        }
    }


    val onGetContactSuccess = remember {
        { emergencyContactModel: EmergencyContactModel ->
            uiState = uiState.copy(
                contactCurrentlyEditing = emergencyContactModel,
                nameErrorMessage = "",
                phoneNumberErrorMessage = "")

            name = name.copy(text = emergencyContactModel.name)
            phoneNumber = phoneNumber.copy(text = emergencyContactModel.phoneNumber)
        }
    }


    LaunchedEffect(key1 = shouldOpenAddNewContact) {
        if (shouldOpenAddNewContact) {
            onClickAddContact()
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
                is EmergencyCallEvent.GetContactSuccess -> onGetContactSuccess(event.emergencyContactModel)
                is EmergencyCallEvent.NameFailure -> onNameFailure(event.message)
                is EmergencyCallEvent.PhoneNumberFailure -> onPhoneFailure(event.message)
                is EmergencyCallEvent.SaveContactSuccess -> {
                    onSaveContactSuccess(); onCloseEditContactScreen()
                }

                is EmergencyCallEvent.UnknownFailure -> onUnknownFailure(event.message)
            }
        }
    }

    EmergencyCallScreenContent(
        uiState = uiState,
        modifier = Modifier.padding(paddingValues),
        state = state,
        keyboardActions = keyboardActions,
        name = name,
        phoneNumber = phoneNumber,
        event = { event ->
            when (event) {
                is EmergencyCallUiEvent.OnClickContact -> onClickContact(event.phoneNumber)
                is EmergencyCallUiEvent.OnClickAddContact -> onClickAddContact()
                is EmergencyCallUiEvent.OnClickDeleteContact -> showDeleteDialog(event.emergencyContact)
                is EmergencyCallUiEvent.OnClickEditContact -> onClickEditContact(event.emergencyContact)
                is EmergencyCallUiEvent.DismissDeleteContactDialog -> dismissDeleteDialog()
                is EmergencyCallUiEvent.DeleteContact -> deleteContact(event.emergencyContact)
                is EmergencyCallUiEvent.DismissMaximumContactDialog -> dismissMaximumDialog()
                is EmergencyCallUiEvent.CancelEditContact -> onCloseEditContactScreen()
                is EmergencyCallUiEvent.DismissEditContactScreen -> onCloseEditContactScreen()
                is EmergencyCallUiEvent.OnChangeName -> onValueChangeName(event.name)
                is EmergencyCallUiEvent.OnChangePhoneNumber -> onValueChangePhoneNumber(event.phoneNumber)
                is EmergencyCallUiEvent.SaveEditContact -> saveAddEditContact()

            }
        })
}