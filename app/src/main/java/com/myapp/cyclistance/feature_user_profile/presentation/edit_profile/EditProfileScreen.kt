package com.myapp.cyclistance.feature_user_profile.presentation.edit_profile

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
import com.google.accompanist.permissions.*
import com.myapp.cyclistance.core.utils.permissions.isGranted
import com.myapp.cyclistance.core.utils.permissions.requestPermission
import com.myapp.cyclistance.core.utils.save_images.ImageUtils
import com.myapp.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.myapp.cyclistance.feature_user_profile.domain.model.UserProfileInfoModel
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.components.EditProfileScreenContent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileEvent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileUiEvent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.event.EditProfileVmEvent
import com.myapp.cyclistance.feature_user_profile.presentation.edit_profile.state.EditProfileUiState
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalComposeUiApi::class,
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
    var uiState by rememberSaveable { mutableStateOf(EditProfileUiState()) }
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var name by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var cyclingGroup by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    var address by rememberSaveable(stateSaver = TextFieldValue.Saver) {
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
        editProfileViewModel.onEvent(event = EditProfileVmEvent.LoadProfile)
        editProfileViewModel.onEvent(event = EditProfileVmEvent.LoadUserProfileInfo)
    }

    LaunchedEffect(true) {

        editProfileViewModel.eventFlow.collect { event ->
            when (event) {

                is EditProfileEvent.UpdateUserProfileSuccess -> {
                    Toast.makeText(context, "Successfully Updated!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

                is EditProfileEvent.GetPhotoUrlSuccess -> {
                    uiState = uiState.copy(photoUrl = event.photoUrl)
                }

                is EditProfileEvent.GetNameSuccess -> {
                    name = TextFieldValue(text = event.name)
                }

                is EditProfileEvent.NameInputFailed -> {
                    uiState = uiState.copy(nameErrorMessage = event.reason)
                }

                is EditProfileEvent.NoInternetConnection -> {
                    uiState = uiState.copy(noInternetVisible = true)
                }

                is EditProfileEvent.InternalServerError -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is EditProfileEvent.UpdateUserProfileFailed -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                }

                is EditProfileEvent.AddressInputFailed -> {
                    uiState = uiState.copy(addressErrorMessage = event.reason)
                }

                is EditProfileEvent.GetAddressSuccess -> {
                    address = TextFieldValue(text = event.address)
                }
                is EditProfileEvent.GetBikeGroupSuccess -> {
                    cyclingGroup = TextFieldValue(text = event.cyclingGroup)
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
    val onValueChangeName = remember {
        { input: TextFieldValue ->
            name = input
            uiState = uiState.copy(nameErrorMessage = "")
        }
    }

    val onValueChangeCyclingGroup = remember {
        { input: TextFieldValue ->
            cyclingGroup = input
            uiState = uiState.copy(cyclingGroupErrorMessage = "")
        }
    }

    val onValueChangeAddress = remember {
        { input: TextFieldValue ->
            address = input
            uiState = uiState.copy(addressErrorMessage = "")
        }
    }



    val saveProfile = remember{{
        editProfileViewModel.onEvent(
            event = EditProfileVmEvent.Save(
                userProfile = UserProfileInfoModel(
                    photoUrl = uiState.selectedImageUri,
                    name = name.text,
                    bikeGroup = cyclingGroup.text,
                    address = address.text,
                    averageRating = 0.0
                )))
    }}

    val keyboardActions = remember {
        KeyboardActions(onDone = {
            saveProfile()
        })
    }
    val cancelEditProfile = remember {
        {
            navController.popBackStack()
            Unit
        }
    }
    val confirmEditProfile = remember {{
            saveProfile()
        }
    }

    val onDismissNoInternetDialog = remember {
        {
            uiState = uiState.copy(noInternetVisible = false)
        }
    }
    val dismissProminentCameraDialog = remember{{
        uiState = uiState.copy(prominentCameraDialogVisible = false)
    }}

    val dismissProminentGalleryDialog = remember{{
        uiState = uiState.copy(prominentGalleryDialogVisible = false)
    }}

    val allowProminentCameraDialog = remember{{
        openCameraPermissionState.requestPermission(onGranted = {
            openCameraResultLauncher.launch()
        }, onDenied = {
            uiState = uiState.copy(cameraPermissionDialogVisible = true)
        })
    }}

    val allowProminentGalleryDialog = remember{{
        galleryPermissionState.requestPermission(onGranted = {
            openGalleryResultLauncher.launch("image/*")
        }, onDenied = {
            uiState = uiState.copy(filesAndMediaDialogVisible = true)
        })
    }}

    EditProfileScreenContent(
        modifier = Modifier
            .padding(paddingValues),
        photoUrl = imageBitmap?.asImageBitmap() ?: uiState.photoUrl,
        state = state,
        keyboardActions = keyboardActions,
        name = name,
        cyclingGroup = cyclingGroup,
        address = address,
        event = { event ->
            when (event) {
                is EditProfileUiEvent.SelectImageFromGallery -> openGallery()
                is EditProfileUiEvent.OpenCamera -> openCamera()
                is EditProfileUiEvent.OnChangeName -> onValueChangeName(event.name)
                is EditProfileUiEvent.CancelEditProfile -> cancelEditProfile()
                is EditProfileUiEvent.ConfirmEditProfile -> confirmEditProfile()
                is EditProfileUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
                is EditProfileUiEvent.ToggleBottomSheet -> {
                    toggleBottomSheet()
                    keyboardController?.hide()
                }

                is EditProfileUiEvent.DismissCameraDialog -> onDismissCameraPermissionDialog()
                is EditProfileUiEvent.DismissFilesAndMediaDialog -> onDismissFilesAndMediaPermissionDialog()
                is EditProfileUiEvent.OnChangeAddress -> onValueChangeAddress(event.address)
                is EditProfileUiEvent.OnChangeCyclingGroup -> onValueChangeCyclingGroup(event.cyclingGroup)
                EditProfileUiEvent.AllowProminentCameraDialog -> allowProminentCameraDialog()
                EditProfileUiEvent.AllowProminentGalleryDialog -> allowProminentGalleryDialog()
                EditProfileUiEvent.DismissProminentCameraDialog -> dismissProminentCameraDialog()
                EditProfileUiEvent.DismissProminentGalleryDialog -> dismissProminentGalleryDialog()
            }
        },
        uiState = uiState,
        bottomSheetScaffoldState = bottomSheetScaffoldState
    )

}

