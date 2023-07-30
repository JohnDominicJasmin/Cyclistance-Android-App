package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.Manifest
import android.annotation.SuppressLint
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
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.EditProfileScreenContent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileUiState
import com.google.accompanist.permissions.*
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by editProfileViewModel.state.collectAsStateWithLifecycle()

    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uiState by remember { mutableStateOf(EditProfileUiState()) }
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var phoneNumber by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }


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


    LaunchedEffect(key1 = true){
        editProfileViewModel.onEvent(event = EditProfileVmEvent.LoadProfile)
    }

    LaunchedEffect(true) {

        editProfileViewModel.eventFlow.collect { event ->
            when (event) {

                is EditProfileEvent.UpdateUserProfileSuccess -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

                is EditProfileEvent.GetPhotoUrlSuccess -> {
                    uiState = uiState.copy(photoUrl = event.photoUrl)
                }

                is EditProfileEvent.GetNameSuccess -> {
                    name = TextFieldValue(text = event.name)
                }

                is EditProfileEvent.GetPhoneNumberSuccess -> {
                    phoneNumber = TextFieldValue(text = event.phoneNumber)
                }

                is EditProfileEvent.GetNameFailed -> {
                    uiState = uiState.copy(nameErrorMessage = event.reason)
                }

                is EditProfileEvent.GetPhoneNumberFailed -> {
                    uiState = uiState.copy(phoneNumberErrorMessage = event.reason)
                }

                is EditProfileEvent.NoInternetConnection -> {
                    uiState = uiState.copy(noInternetVisible = true)
                }

                is EditProfileEvent.InternalServerError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

            }
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
        { input: TextFieldValue ->
            name = input
            uiState = uiState.copy(nameErrorMessage = "")
        }
    }
    val onValueChangePhoneNumber = remember {
        { input: TextFieldValue ->
            phoneNumber = input
            uiState = uiState.copy(phoneNumberErrorMessage = "")
        }
    }
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            editProfileViewModel.onEvent(
                event = EditProfileVmEvent.Save(
                    imageUri = uiState.selectedImageUri,
                    name = name.text,
                    phoneNumber = phoneNumber.text))
        })
    }
    val cancelEditProfile = remember {
        {
            navController.popBackStack()
            Unit
        }
    }
    val confirmEditProfile = remember {
        {
            editProfileViewModel.onEvent(
                event = EditProfileVmEvent.Save(
                    imageUri = uiState.selectedImageUri,
                    name = name.text,
                    phoneNumber = phoneNumber.text))
        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(noInternetVisible = false)
        }
    }

    EditProfileScreenContent(
        modifier = Modifier
            .padding(paddingValues),
        photoUrl = imageBitmap?.asImageBitmap() ?: uiState.photoUrl,
        state = state,
        keyboardActions = keyboardActions,
        name = name,
        phoneNumber = phoneNumber,
        event = { event ->
            when (event) {
                is EditProfileUiEvent.SelectImageFromGallery -> openGallery()
                is EditProfileUiEvent.OpenCamera -> openCamera()
                is EditProfileUiEvent.OnChangeName -> onValueChangeName(event.name)
                is EditProfileUiEvent.OnChangePhoneNumber -> onValueChangePhoneNumber(event.phoneNumber)
                is EditProfileUiEvent.CancelEditProfile -> cancelEditProfile()
                is EditProfileUiEvent.ConfirmEditProfile -> confirmEditProfile()
                is EditProfileUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is EditProfileUiEvent.ToggleBottomSheet -> {
                    toggleBottomSheet()
                    keyboardController?.hide()
                }

                is EditProfileUiEvent.DismissCameraDialog -> onDismissCameraPermissionDialog()
                is EditProfileUiEvent.DismissFilesAndMediaDialog -> onDismissFilesAndMediaPermissionDialog()
            }
        },
        uiState = uiState,
        bottomSheetScaffoldState = bottomSheetScaffoldState
    )

}

