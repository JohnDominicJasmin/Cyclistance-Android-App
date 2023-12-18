package com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact

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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.myapp.cyclistance.core.utils.permissions.isGranted
import com.myapp.cyclistance.core.utils.permissions.requestPermission
import com.myapp.cyclistance.core.utils.save_images.ImageUtils
import com.myapp.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.myapp.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.components.AddEditContactContent
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactUiEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactVmEvent
import com.myapp.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
    ExperimentalPermissionsApi::class)
@Composable
fun AddEditContactScreen(
    viewModel: AddEditContactViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues) {


    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()


    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var phoneNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var uiState by rememberSaveable {
        mutableStateOf(value = AddEditContactUiState())
    }

    val scope = rememberCoroutineScope()
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
                val imageUri = if (imageBitmap == null) uri.toString() else ImageUtils.encodeImage(
                    imageBitmap!!)

                uiState = uiState.copy(selectedImageUri = imageUri)
            }
        }

    val openCameraResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            val uri = bitmap?.toImageUri().toString()
            imageBitmap = bitmap
            val imageUri = if (imageBitmap == null) uri else ImageUtils.encodeImage(
                imageBitmap!!)

            imageUri.takeIf { it != "null" && it.isNotEmpty() }?.let{
                uiState = uiState.copy(selectedImageUri = imageUri)
            }

        }

    val galleryPermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE))


    val openCameraPermissionState =
        rememberPermissionState(permission = Manifest.permission.CAMERA)

    val openGallery = remember {
        {
            if(galleryPermissionState.allPermissionsGranted){
                openGalleryResultLauncher.launch("image/*")
            }else{
                uiState = uiState.copy(prominentGalleryDialogVisible = true)
            }
        }
    }



    val openCamera = remember {
        {

            if(openCameraPermissionState.hasPermission){
                openCameraResultLauncher.launch()
            }else{
                uiState = uiState.copy(prominentCameraDialogVisible = true)
            }

        }
    }



    val dismissGalleryPermissionDialog = remember {
        {
            uiState = uiState.copy(filesAndMediaPermissionDialogVisible = false)
        }
    }

    val dismissCameraPermissionDialog = remember {
        {
            uiState = uiState.copy(cameraPermissionDialogVisible = false)
        }
    }


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
                event = AddEditContactVmEvent.SaveContact(
                    emergencyContactModel = EmergencyContactModel(
                        name = name.text,
                        phoneNumber = phoneNumber.text,
                        photo = uiState.selectedImageUri ?: state.emergencyContact?.photo ?: ""
                    )
                ))
        }
    }
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            saveAddEditContact()
        })
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
    val onSaveContactSuccess = remember {
        {
            Toast.makeText(context, "Contact Saved", Toast.LENGTH_LONG).show()
        }
    }
    val onCloseEditContactScreen = remember{{
        navController.popBackStack()
    }}

    val onUnknownFailure = remember {
        { message: String ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    val onGetContactSuccess = remember {
        { emergencyContactModel: EmergencyContactModel ->
            uiState = uiState.copy(
                nameErrorMessage = "",
                phoneNumberErrorMessage = "")


        }
    }
    val dismissProminentCameraDialog = remember{{
        uiState = uiState.copy(prominentCameraDialogVisible = false)
    }}

    val dismissProminentGalleryDialog = remember{{
        uiState = uiState.copy(prominentGalleryDialogVisible = false)
    }}

    val allowProminentCameraDialog = remember{{
        dismissProminentCameraDialog()
        openCameraPermissionState.requestPermission(onGranted = {
            openCameraResultLauncher.launch()
        }, onDenied = {
            uiState = uiState.copy(cameraPermissionDialogVisible = true)
        })
    }}

    val allowProminentGalleryDialog = remember{{
        dismissProminentGalleryDialog()
        galleryPermissionState.requestPermission(onGranted = {
            openGalleryResultLauncher.launch("image/*")
        }, onDenied = {
            uiState = uiState.copy(filesAndMediaPermissionDialogVisible = true)
        })
    }}



    LaunchedEffect(key1 = galleryPermissionState.isGranted()){
        if(galleryPermissionState.isGranted()){
            openGalleryResultLauncher.launch("image/*")
        }
    }

    LaunchedEffect(key1 = openCameraPermissionState.isGranted()){
        if(openCameraPermissionState.isGranted()){
            openCameraResultLauncher.launch()
        }
    }

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collect { event ->
            when(event){
                is AddEditContactEvent.NameFailure ->  onNameFailure(event.message)
                is AddEditContactEvent.PhoneNumberFailure -> onPhoneFailure(event.message)
                AddEditContactEvent.SaveContactSuccess ->  {
                    onSaveContactSuccess(); onCloseEditContactScreen()
                }
                is AddEditContactEvent.UnknownFailure -> onUnknownFailure(event.message)
                is AddEditContactEvent.GetContactSuccess -> onGetContactSuccess(event.emergencyContact)
            }
        }
    }

    LaunchedEffect(key1 = state.emergencyContact){
        state.emergencyContact?.name?.let{
            name = TextFieldValue(text = it)
        }
        state.emergencyContact?.phoneNumber?.let{
            phoneNumber = TextFieldValue(text = it)
        }
    }


    AddEditContactContent(
        modifier = Modifier.padding(paddingValues),
        bottomSheetScaffoldState = bottomSheetScaffoldState,
        state = state,
        name = name,
        phoneNumber = phoneNumber,
        uiState = uiState,
        keyboardActions = keyboardActions,
        event = { event ->
            when(event){
                AddEditContactUiEvent.CancelEditContact -> onCloseEditContactScreen()
                AddEditContactUiEvent.DismissCameraDialog -> dismissCameraPermissionDialog()
                AddEditContactUiEvent.DismissFilesAndMediaDialog -> dismissGalleryPermissionDialog()
                is AddEditContactUiEvent.OnChangeName -> onValueChangeName(event.name)
                is AddEditContactUiEvent.OnChangePhoneNumber -> onValueChangePhoneNumber(event.phoneNumber)
                AddEditContactUiEvent.OpenCamera -> openCamera()
                AddEditContactUiEvent.SaveEditContact -> saveAddEditContact()
                AddEditContactUiEvent.SelectImageFromGallery -> openGallery()
                AddEditContactUiEvent.ToggleBottomSheet -> {
                    toggleBottomSheet()
                    keyboardController?.hide()
                }

                AddEditContactUiEvent.AllowProminentCameraDialog -> allowProminentCameraDialog()
                AddEditContactUiEvent.AllowProminentGalleryDialog -> allowProminentGalleryDialog()
                AddEditContactUiEvent.DismissProminentCameraDialog -> dismissProminentCameraDialog()
                AddEditContactUiEvent.DismissProminentGalleryDialog -> dismissProminentGalleryDialog()
            }
        },)



}