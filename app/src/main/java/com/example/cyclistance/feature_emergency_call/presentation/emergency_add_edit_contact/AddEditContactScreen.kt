package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.permissions.requestPermission
import com.example.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.components.AddEditContactContent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditContactUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditVmEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactUiState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class)
@Composable
fun EmergencyAddEditContactScreen(
    navController: NavController,
    paddingValues: PaddingValues,
    viewModel: AddEditContactViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(AddEditContactUiState()) }
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val toggleBottomSheet = remember(bottomSheetScaffoldState, state.isLoading) {
        {
            scope.launch {

                if (!state.isLoading) {
                    with(bottomSheetScaffoldState) {
                        if (isVisible) {
                            hide()
                        } else {
                            show()
                        }
                    }
                }
            }
        }
    }
    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uiState = uiState.copy(selectedImageUri = uri.toString())
            uri?.let { selectedUri ->
                imageBitmap =
                    when {
                        Build.VERSION.SDK_INT < Build.VERSION_CODES.P -> {
                            MediaStore.Images.Media.getBitmap(
                                context.contentResolver,
                                selectedUri)
                        }

                        else -> {
                            val source =
                                ImageDecoder.createSource(
                                    context.contentResolver,
                                    selectedUri)
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
            }


        }

    val openCameraResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            val uri = bitmap?.toImageUri().toString()
            imageBitmap = bitmap
            uiState = uiState.copy(selectedImageUri = uri)

        }


    val filesAndMediaPermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) { permissionGranted ->
            if (permissionGranted.values.all { it }) {
                openGalleryResultLauncher.launch("image/*")
            }
        }


    val openCameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA) { permissionGranted ->

            if (permissionGranted) {
                openCameraResultLauncher.launch()
            }
        }


    val onDismissFilesAndMediaPermissionDialog = remember {
        {
            uiState = uiState.copy(filesAndMediaDialogVisible = false)
        }
    }

    val onDismissCameraPermissionDialog = remember {
        {
            uiState = uiState.copy(cameraPermissionDialogVisible = false)
        }
    }

    val openGallery = remember {
        {
            filesAndMediaPermissionState.requestPermission(
                onGranted = {
                    openGalleryResultLauncher.launch("image/*")
                }, onExplain = {
                    uiState = uiState.copy(filesAndMediaDialogVisible = true)
                })

        }
    }

    val openCamera = remember {
        {
            openCameraPermissionState.requestPermission(
                onGranted = {
                    openCameraResultLauncher.launch()
                }, onExplain = {
                    uiState = uiState.copy(cameraPermissionDialogVisible = true)
                })
        }
    }

    val onValueChangeName = remember {
        { name: TextFieldValue ->
            uiState = uiState.copy(name = name, nameErrorMessage = "")
        }
    }
    val onValueChangePhoneNumber = remember {
        { phoneNumber: TextFieldValue ->
            uiState = uiState.copy(phoneNumber = phoneNumber, phoneNumberErrorMessage = "")
        }
    }

    val saveAddEditContact = remember {
        {
            viewModel.onEvent(
                event = AddEditVmEvent.SaveContact(
                    emergencyContactModel = EmergencyContactModel(
                        id = uiState.contactCurrentlyEditing?.id ?: 0,
                        name = uiState.name.text,
                        phoneNumber = uiState.phoneNumber.text,
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


    val onCloseEditContactScreen = remember {
        {
            navController.popBackStack()
            Unit
        }
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
                photoUrl = emergencyContactModel.photo,
                name = TextFieldValue(emergencyContactModel.name),
                phoneNumber = TextFieldValue(emergencyContactModel.phoneNumber)
            )
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditEvent.GetContactSuccess -> onGetContactSuccess(event.emergencyContactModel)
                is AddEditEvent.SaveContactSuccess -> {
                    onSaveContactSuccess(); onCloseEditContactScreen()
                }

                is AddEditEvent.NameFailure -> onNameFailure(event.message)
                is AddEditEvent.PhoneNumberFailure -> onPhoneFailure(event.message)
                is AddEditEvent.UnknownFailure -> onUnknownFailure(event.message)
            }
        }
    }


    AddEditContactContent(
        modifier = Modifier.padding(paddingValues),
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        keyboardActions = keyboardActions,
        state = state,
        uiState = uiState,
        photoUrl = imageBitmap?.asImageBitmap() ?: uiState.photoUrl,
        event = { event ->
            when (event) {
                is AddEditContactUiEvent.SelectImageFromGallery -> openGallery()
                is AddEditContactUiEvent.OpenCamera -> openCamera()
                is AddEditContactUiEvent.OnChangeName -> onValueChangeName(event.name)
                is AddEditContactUiEvent.OnChangePhoneNumber -> onValueChangePhoneNumber(event.phoneNumber)
                is AddEditContactUiEvent.CancelEditContact -> onCloseEditContactScreen()
                is AddEditContactUiEvent.SaveEditContact -> saveAddEditContact()
                is AddEditContactUiEvent.ToggleBottomSheet -> {
                    toggleBottomSheet()
                    keyboardController?.hide()
                }

                is AddEditContactUiEvent.DismissFilesAndMediaDialog -> onDismissCameraPermissionDialog()
                is AddEditContactUiEvent.DismissCameraDialog -> onDismissFilesAndMediaPermissionDialog()
                is AddEditContactUiEvent.CloseEditContactScreen -> onCloseEditContactScreen()
            }
        }

    )

}

