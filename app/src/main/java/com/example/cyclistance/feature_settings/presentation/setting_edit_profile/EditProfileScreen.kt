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
    val (selectedImageUri, onChangeSelectedImageUri) = remember { mutableStateOf("") }

    val (photoUrl, onChangePhotoUrl) = remember { mutableStateOf("") }



    val (name, onChangeName) = remember { mutableStateOf("") }
    val (nameErrorMessage, onChangeNameError) = remember { mutableStateOf("") }

    val (phoneNumber, onChangePhoneNumber) = remember { mutableStateOf("") }
    val (phoneNumberErrorMessage, onChangePhoneNumberError) = remember { mutableStateOf("") }

    val (isNoInternetVisible, onChangeNoInternetVisibility) = rememberSaveable {
        mutableStateOf(false)
    }

    val isUserInformationChanges by remember {
        derivedStateOf {
            name != state.nameSnapshot ||
            phoneNumber != state.phoneNumberSnapshot ||
            selectedImageUri.isNotEmpty()
        }
    }


    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onChangeSelectedImageUri(uri.toString())
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
            onChangeSelectedImageUri(uri)

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
                is EditProfileUiEvent.UpdateUserProfileSuccess -> {
                    Toast.makeText(context, event.reason, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }

                is EditProfileUiEvent.GetPhotoUrlSuccess -> {
                    onChangePhotoUrl(event.photoUrl)
                }

                is EditProfileUiEvent.GetNameSuccess -> {
                    onChangeName(event.name)
                }

                is EditProfileUiEvent.GetNameFailed -> {
                    onChangeNameError(event.reason)
                }

                is EditProfileUiEvent.GetPhoneNumberSuccess -> {
                    onChangePhoneNumber(event.phoneNumber)
                }

                is EditProfileUiEvent.GetPhoneNumberFailed -> {
                    onChangePhoneNumberError(event.reason)
                }

                is EditProfileUiEvent.NoInternetConnection -> {
                    onChangeNoInternetVisibility(true)
                }


            }
        }

    }


    val onClickGalleryButton = remember {
        {
            accessStoragePermissionState.requestPermission(
                context = context,
                rationalMessage = "Storage permission is not yet granted.") {
                openGalleryResultLauncher.launch("image/*")
            }

        }
    }

    val onClickCameraButton = remember {
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
            onChangeName(name)
        }
    }
    val onValueChangePhoneNumber = remember {
        { phoneNumber: String ->
            onChangePhoneNumberError(phoneNumber)
        }
    }
    val keyboardActions = remember {
        KeyboardActions(onDone = {
            editProfileViewModel.onEvent(
                event = EditProfileEvent.Save(
                    imageUri = selectedImageUri,
                    name = name,
                    phoneNumber = phoneNumber))
        })
    }
    val onClickCancelButton = remember {
        {
            navController.popBackStack()
            Unit
        }
    }
    val onClickConfirmButton = remember {
        {
            editProfileViewModel.onEvent(
                event = EditProfileEvent.Save(
                    imageUri = selectedImageUri,
                    name = name,
                    phoneNumber = phoneNumber))
        }
    }

    val onDismissNoInternetDialog = remember {
        {
            onChangeNoInternetVisibility(false)
        }
    }

    EditProfileScreenContent(
        modifier = Modifier
            .padding(paddingValues),
        photoUrl = imageBitmap?.asImageBitmap() ?: photoUrl,
        onClickGalleryButton = onClickGalleryButton,
        onClickCameraButton = onClickCameraButton,
        state = state,
        onValueChangeName = onValueChangeName,
        onValueChangePhoneNumber = onValueChangePhoneNumber,
        keyboardActions = keyboardActions,
        onClickCancelButton = onClickCancelButton,
        onClickConfirmButton = onClickConfirmButton,
        onDismissNoInternetDialog = onDismissNoInternetDialog,
        isNoInternetVisible = isNoInternetVisible,
        name = name,
        nameErrorMessage = nameErrorMessage,
        phoneNumber = phoneNumber,
        phoneNumberErrorMessage = phoneNumberErrorMessage,
        isUserInformationChanges = isUserInformationChanges
    )

}

@Preview
@Composable
fun EditProfilePreview() {
    CyclistanceTheme(true) {
        EditProfileScreenContent(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(
                isLoading = false),
            isNoInternetVisible = false,
            name = "John Doe",
            nameErrorMessage = "Sample Error",
            phoneNumber = "09123456789",
            phoneNumberErrorMessage = "Sample Error",
            isUserInformationChanges = true
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreenContent(
    modifier: Modifier,
    photoUrl: Any,
    state: EditProfileState = EditProfileState(),
    isNoInternetVisible: Boolean,
    isUserInformationChanges: Boolean,
    name: String,
    nameErrorMessage: String,
    phoneNumber: String,
    phoneNumberErrorMessage: String,
    onClickGalleryButton: () -> Unit = {},
    onClickCameraButton: () -> Unit = {},
    onValueChangeName: (String) -> Unit = {},
    onValueChangePhoneNumber: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions { },
    onClickCancelButton: () -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
    onDismissNoInternetDialog: () -> Unit = {},
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



    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        SelectImageBottomSheet(
            onClickGalleryButton = {
                toggleBottomSheet()
                onClickGalleryButton()
            },
            onClickCameraButton = {
                toggleBottomSheet()
                onClickCameraButton()
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
                    onValueChangeName = onValueChangeName,
                    onValueChangePhoneNumber = onValueChangePhoneNumber,
                    keyboardActions = keyboardActions,
                    name = name,
                    nameErrorMessage = nameErrorMessage,
                    phoneNumber = phoneNumber,
                    phoneNumberErrorMessage = phoneNumberErrorMessage,

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
                    onClickCancelButton = onClickCancelButton,
                    onClickConfirmButton = onClickConfirmButton,
                    negativeButtonEnabled = !state.isLoading,
                    positiveButtonEnabled = !state.isLoading && isUserInformationChanges,
                )

                if (isNoInternetVisible) {

                    NoInternetDialog(
                        modifier = Modifier.constrainAs(noInternetDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.matchParent
                            height = Dimension.wrapContent
                        }, onDismiss = onDismissNoInternetDialog
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


