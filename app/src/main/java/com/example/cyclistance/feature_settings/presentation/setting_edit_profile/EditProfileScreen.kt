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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.example.cyclistance.core.utils.permission.requestPermission
import com.example.cyclistance.core.utils.save_images.ImageUtils.saveImageToGallery
import com.example.cyclistance.core.utils.save_images.ImageUtils.toImageUri
import com.example.cyclistance.feature_mapping.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.ProfilePictureArea
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.TextFieldInputArea
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.utils.isUserInformationChanges
import com.example.cyclistance.theme.Blue600
import com.example.cyclistance.theme.CyclistanceTheme
import com.google.accompanist.permissions.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel,
    navController: NavController,
    paddingValues: PaddingValues
) {

    val state by editProfileViewModel.state.collectAsState()


    val context = LocalContext.current

    var imageUri by remember{ mutableStateOf<Uri?>(null) }
    var imageBitmap by remember{ mutableStateOf<Bitmap?>(null) }

    val saveImageToGallery = remember {{ bitmap: Bitmap ->
        context.saveImageToGallery(bitmap = bitmap)
    }}

    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            editProfileViewModel.onEvent(event = EditProfileEvent.SelectImageUri(uri = uri.toString()))
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
            editProfileViewModel.onEvent(event = EditProfileEvent.SelectImageUri(uri = uri))
            bitmap?.let(saveImageToGallery)
        }


    val accessStoragePermissionState =
        rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) { permissionGranted ->
            if (state.galleryButtonClick && permissionGranted.values.all { it }) {
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

        editProfileViewModel.onEvent(event = EditProfileEvent.LoadName)
        editProfileViewModel.onEvent(event = EditProfileEvent.LoadPhoto)
        editProfileViewModel.onEvent(event = EditProfileEvent.LoadPhoneNumber)

        if (!accessStoragePermissionState.allPermissionsGranted) {
            accessStoragePermissionState.launchMultiplePermissionRequest()
        }

        editProfileViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditProfileUiEvent.CloseScreen -> {
                    navController.popBackStack()
                }
                is EditProfileUiEvent.ShowToastMessage -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

            }
        }

    }


    val onClickGalleryButton = remember{{
        editProfileViewModel.onEvent(event = EditProfileEvent.OnClickGalleryButton)
        accessStoragePermissionState.requestPermission(
            context = context,
            rationalMessage = "Storage permission is not yet granted.") {
            openGalleryResultLauncher.launch("image/*")
        }

    }}

    val onClickCameraButton = remember{{
        openCameraPermissionState.requestPermission(
            context = context,
            rationalMessage = "Camera permission is not yet granted.") {
            openCameraResultLauncher.launch()
        }
    }}
    val onValueChangeName = remember{{ name: String ->
        editProfileViewModel.onEvent(
            event = EditProfileEvent.EnterName(name = name))
    }}
    val onValueChangePhoneNumber = remember{{ phoneNumber: String ->
        editProfileViewModel.onEvent(
            event = EditProfileEvent.EnterPhoneNumber(phoneNumber = phoneNumber))
    }}
    val keyboardActions = remember{ KeyboardActions(onDone = {
        editProfileViewModel.onEvent(event = EditProfileEvent.Save)
    })}
    val onClickCancelButton = remember {{
        navController.popBackStack()
        Unit
    }}
    val onClickConfirmButton = remember {{
        editProfileViewModel.onEvent(event = EditProfileEvent.Save)
    }}

    EditProfileScreenContent(
        modifier = Modifier
            .padding(paddingValues),
        photoUrl = imageBitmap?.asImageBitmap() ?: state.photoUrl,
        onClickGalleryButton = onClickGalleryButton,
        onClickCameraButton = onClickCameraButton,
        state = state,
        onValueChangeName = onValueChangeName,
        onValueChangePhoneNumber = onValueChangePhoneNumber,
        keyboardActions = keyboardActions,
        onClickCancelButton = onClickCancelButton,
        onClickConfirmButton = onClickConfirmButton)

}

@Preview
@Composable
fun EditProfilePreview() {
    CyclistanceTheme(true) {
        EditProfileScreenContent(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(isLoading = false, nameErrorMessage = "Field cannot be blank", phoneNumberErrorMessage = "Field cannot be blank"))
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreenContent(
    modifier: Modifier,
    photoUrl: Any,
    state: EditProfileState = EditProfileState(),
    onClickGalleryButton: () -> Unit = {},
    onClickCameraButton: () -> Unit = {},
    onValueChangeName: (String) -> Unit = {},
    onValueChangePhoneNumber: (String) -> Unit = {},
    keyboardActions: KeyboardActions = KeyboardActions { },
    onClickCancelButton: () -> Unit = {},
    onClickConfirmButton: () -> Unit = {},
) {


    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val toggleBottomSheet = remember(bottomSheetScaffoldState, state.isLoading) {{
        scope.launch {
            if (!state.isLoading) {
                with(bottomSheetScaffoldState) {
                    if (isVisible) hide() else show()
                }
            }
        }
    }}




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
                positiveButtonEnabled = !state.isLoading && state.isUserInformationChanges(),
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

