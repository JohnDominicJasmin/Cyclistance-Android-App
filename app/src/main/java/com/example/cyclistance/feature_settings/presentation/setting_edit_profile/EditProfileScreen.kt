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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.example.cyclistance.feature_alert_dialog.presentation.NoInternetDialog
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.ProfilePictureArea
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldInputArea
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileVmEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileState
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileUiState
import com.example.cyclistance.theme.Blue600
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by editProfileViewModel.state.collectAsStateWithLifecycle()


    val context = LocalContext.current

    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var uiState by rememberSaveable{ mutableStateOf(EditProfileUiState()) }


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
            val uri = bitmap?.toImageUri(context).toString()
            imageBitmap = bitmap
            uiState = uiState.copy(selectedImageUri = uri)

        }


    val accessStoragePermissionState =
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




    LaunchedEffect(true) {


        editProfileViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditProfileEvent.UpdateUserProfileSuccess -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

                is EditProfileEvent.GetPhotoUrlSuccess -> {
                    uiState = uiState.copy(photoUrl = event.photoUrl)
                }

                is EditProfileEvent.GetNameSuccess -> {
                    uiState = uiState.copy(name = event.name)
                }

                is EditProfileEvent.GetNameFailed -> {
                    uiState = uiState.copy(nameErrorMessage = event.reason)
                }

                is EditProfileEvent.GetPhoneNumberSuccess -> {
                    uiState = uiState.copy(phoneNumber = event.phoneNumber)
                }

                is EditProfileEvent.GetPhoneNumberFailed -> {
                    uiState = uiState.copy(phoneNumberErrorMessage = event.reason)
                }

                is EditProfileEvent.NoInternetConnection -> {
                    uiState = uiState.copy(noInternetVisible = true)
                }


            }
        }

    }


    val openGallery = remember {
        {
            accessStoragePermissionState.requestPermission(
                context = context,
                rationalMessage = "Storage permission is not yet granted.") {
                openGalleryResultLauncher.launch("image/*")
            }

        }
    }

    val openCamera = remember {
        {
            openCameraPermissionState.requestPermission(
                context = context,
                rationalMessage = "Camera permission is not yet granted.") {
                openCameraResultLauncher.launch()
            }
        }
    }

    val onValueChangeName = remember {
        { name: String ->
            uiState = uiState.copy(name = name)
        }
    }
    val onValueChangePhoneNumber = remember {
        { phoneNumber: String ->
            uiState = uiState.copy(phoneNumber = phoneNumber)
        }
    }
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            editProfileViewModel.onEvent(
                event = EditProfileVmEvent.Save(
                    imageUri = uiState.selectedImageUri,
                    name = uiState.name,
                    phoneNumber = uiState.phoneNumber))
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
                    name = uiState.name,
                    phoneNumber = uiState.phoneNumber))
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
        event = { event ->
            when(event){
                is EditProfileUiEvent.SelectImageFromGallery -> openGallery()
                is EditProfileUiEvent.OpenCamera -> openCamera()
                is EditProfileUiEvent.ChangeName -> onValueChangeName(event.name)
                is EditProfileUiEvent.ChangePhoneNumber -> onValueChangePhoneNumber(event.phoneNumber)
                is EditProfileUiEvent.CancelEditProfile -> cancelEditProfile()
                is EditProfileUiEvent.ConfirmEditProfile -> confirmEditProfile()
                is EditProfileUiEvent.DismissNoInternetDialog -> onDismissNoInternetDialog()
            }
        }


    )

}

@Preview
@Composable
fun EditProfilePreview() {

    CyclistanceTheme(true) {
        EditProfileScreenContent(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(isLoading = false),
            uiState = EditProfileUiState())
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreenContent(
    modifier: Modifier,
    photoUrl: Any,
    state: EditProfileState = EditProfileState(),
    keyboardActions: KeyboardActions = KeyboardActions { },
    uiState: EditProfileUiState = EditProfileUiState(),
    event : (EditProfileUiEvent) -> Unit = {}
) {


    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val toggleBottomSheet = remember(bottomSheetScaffoldState, state.isLoading) {
        {
            scope.launch {
                if (!state.isLoading) {
                    with(bottomSheetScaffoldState) {
                        if (isVisible) hide() else show()
                    }
                }
            }
        }
    }

    val isUserInformationChanges by remember(
        uiState.name,
        uiState.phoneNumber,
        uiState.selectedImageUri) {
        derivedStateOf {
            uiState.name != state.nameSnapshot ||
            uiState.phoneNumber != state.phoneNumberSnapshot ||
            uiState.selectedImageUri.isNotEmpty()
        }
    }


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        SelectImageBottomSheet(
            onClickGalleryButton = {
                toggleBottomSheet()
                event(EditProfileUiEvent.SelectImageFromGallery)
            },
            onClickCameraButton = {
                toggleBottomSheet()
                event(EditProfileUiEvent.OpenCamera)
            },
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            editProfileState = state) {

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText, progressBar, noInternetDialog) = createRefs()

                ProfilePictureArea(
                    photoUrl = photoUrl,
                    modifier = Modifier
                        .size(125.dp)
                        .constrainAs(profilePictureArea) {

                            top.linkTo(parent.top, margin = 30.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        },
                    onClick = {
                        toggleBottomSheet()
                    })




                ClickableText(text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(color = Blue600, fontWeight = FontWeight.SemiBold)) {
                        append("Change Profile Photo")
                    }
                },
                    modifier = Modifier.constrainAs(changePhotoText) {
                        top.linkTo(profilePictureArea.bottom, margin = 12.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    },
                    style = MaterialTheme.typography.body2,
                    onClick = {
                        toggleBottomSheet()
                    })





                TextFieldInputArea(
                    modifier = Modifier
                        .constrainAs(textFieldInputArea) {
                            top.linkTo(changePhotoText.bottom, margin = 30.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.percent(0.5f)
                            width = Dimension.percent(0.9f)

                        },
                    state = state,
                    onValueChangeName = {event(EditProfileUiEvent.ChangeName(it))},
                    onValueChangePhoneNumber = {event(EditProfileUiEvent.ChangePhoneNumber(it))},
                    keyboardActions = keyboardActions,
                    name = uiState.name,
                    nameErrorMessage = uiState.nameErrorMessage,
                    phoneNumber = uiState.phoneNumber,
                    phoneNumberErrorMessage = uiState.phoneNumberErrorMessage,

                    )



                MappingButtonNavigation(
                    modifier = Modifier
                        .constrainAs(buttonNavigationArea) {
                            top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 50.dp)
                            height = Dimension.percent(0.1f)
                            width = Dimension.percent(0.8f)
                        },
                    positiveButtonText = "Save",
                    onClickCancelButton = {event(EditProfileUiEvent.CancelEditProfile)},
                    onClickConfirmButton = {event(EditProfileUiEvent.ConfirmEditProfile)},
                    negativeButtonEnabled = !state.isLoading,
                    positiveButtonEnabled = !state.isLoading && isUserInformationChanges,
                )

                if (uiState.noInternetVisible) {

                    NoInternetDialog(
                        modifier = Modifier.constrainAs(noInternetDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        }, onDismiss = {event(EditProfileUiEvent.DismissNoInternetDialog)}
                    )

                }

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(progressBar) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        this.centerTo(parent)
                    })
                }
            }
        }
    }
}


