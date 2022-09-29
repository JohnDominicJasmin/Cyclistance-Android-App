package com.example.cyclistance.feature_settings.presentation.setting_edit_profile

import android.Manifest
import android.app.Activity
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
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
import androidx.navigation.NavController
import com.example.cyclistance.core.utils.MappingConstants.NONE_OF_THE_ABOVE_RESULT_CODE
import com.example.cyclistance.core.utils.MappingConstants.NO_SIM_CARD_RESULT_CODE
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.ProfilePictureArea
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldInputArea
import com.example.cyclistance.theme.Blue600
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.*
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by editProfileViewModel.state.collectAsState()
    val (isGalleryButtonClick, onClickGalleryButton) = rememberSaveable { mutableStateOf(false) }


    val context = LocalContext.current
    val hintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build()

    val simCardIntent = Credentials.getClient(context).getHintPickerIntent(hintRequest)
    val intentSender = IntentSenderRequest.Builder(simCardIntent.intentSender).build()

    val simCardResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()) { result ->

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val credential: Credential? =
                    result.data?.getParcelableExtra(Credential.EXTRA_KEY)
                editProfileViewModel.onEvent(
                    event = EditProfileEvent.EnterPhoneNumber(
                        phoneNumber = credential!!.id))
            }
            NO_SIM_CARD_RESULT_CODE -> {
                Toast.makeText(context, "No SIM Card Detected", Toast.LENGTH_LONG).show()
            }
            NONE_OF_THE_ABOVE_RESULT_CODE -> {
                editProfileViewModel.onEvent(
                    event = EditProfileEvent.EnterPhoneNumber(
                        phoneNumber = ""))
            }
        }
    }

    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            editProfileViewModel.onEvent(event = EditProfileEvent.SelectImageUri(uri = uri))
            uri?.let { selectedUri ->
                editProfileViewModel.onEvent(
                    event = EditProfileEvent.SelectBitmapPicture(
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
                    ))
            }
        }

    val openCameraResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->

            editProfileViewModel.onEvent(event = EditProfileEvent.SelectBitmapPicture(bitmap = bitmap))
            editProfileViewModel.onEvent(event = EditProfileEvent.SaveImageToGallery)

        }


    val accessStoragePermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) { permissionGranted ->
            if (isGalleryButtonClick && permissionGranted.values.all { it }) {
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
        if (!accessStoragePermissionState.allPermissionsGranted) {
            accessStoragePermissionState.launchMultiplePermissionRequest()
        }
        editProfileViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditProfileUiEvent.ShowMappingScreen -> {
                    navController.popBackStack()
                }
                is EditProfileUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }



    EditProfileScreen(
        modifier = Modifier
            .padding(paddingValues),
        photoUrl = state.imageBitmap.bitmap?.asImageBitmap() ?: state.photoUrl,

        onClickGalleryButton = {
            onClickGalleryButton(!isGalleryButtonClick)
            if (accessStoragePermissionState.allPermissionsGranted) {
                openGalleryResultLauncher.launch("image/*")
                return@EditProfileScreen
            }
            if (accessStoragePermissionState.shouldShowRationale || !accessStoragePermissionState.allPermissionsGranted) {
                Toast.makeText(
                    context,
                    "Storage permission is not yet granted.",
                    Toast.LENGTH_SHORT).show()
                return@EditProfileScreen
            }
            accessStoragePermissionState.launchMultiplePermissionRequest()
        },

        onClickCameraButton = {
            if (openCameraPermissionState.status.isGranted) {
                openCameraResultLauncher.launch()
                return@EditProfileScreen
            }
            if (openCameraPermissionState.status.shouldShowRationale || !openCameraPermissionState.status.isGranted) {
                Toast.makeText(context, "Camera permission is not yet granted.", Toast.LENGTH_SHORT)
                    .show()
                return@EditProfileScreen
            }
            openCameraPermissionState.launchPermissionRequest()
        },
        state = state,
        onClickPhoneTextField = {
            simCardResultLauncher.launch(intentSender)
        },
        onValueChangeName = { name ->
            editProfileViewModel.onEvent(
                event = EditProfileEvent.EnterName(name = name))

        },
        onValueChangePhoneNumber = { phoneNumber ->
            editProfileViewModel.onEvent(
                event = EditProfileEvent.EnterPhoneNumber(phoneNumber = phoneNumber))

        },
        keyboardActions = KeyboardActions(onDone = {
            editProfileViewModel.onEvent(event = EditProfileEvent.Save)
        }),
        onClickCancelButton = {
            navController.popBackStack()
        },
        onClickConfirmButton = {
            editProfileViewModel.onEvent(event = EditProfileEvent.Save)
        })

}

@Preview
@Composable
fun EditProfilePreview() {
    CyclistanceTheme(true) {
        EditProfileScreen(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(isLoading = false))
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreen(
    modifier: Modifier,
    photoUrl: Any,
    state: EditProfileState = EditProfileState(),
    onClickGalleryButton: () -> Unit = {},
    onClickCameraButton: () -> Unit = {},
    onClickPhoneTextField: () -> Unit = {},
    onValueChangeName: (String) -> Unit = {},
    onValueChangePhoneNumber: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions { },
    onClickCancelButton: () -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
) {


    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val toggleBottomSheet = {
        scope.launch {
            if (!state.isLoading) {
                with(bottomSheetScaffoldState) {
                    if (isVisible) hide() else show()
                }
            }
        }
    }

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
        editProfileState = state){

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {


        val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText, progressBar) = createRefs()

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
            onClickPhoneTextField = onClickPhoneTextField,
            onValueChangeName = onValueChangeName,
            onValueChangePhoneNumber = onValueChangePhoneNumber,
            keyboardActions = keyboardActions,
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
            positiveButtonEnabled = !state.isLoading,
        )

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

