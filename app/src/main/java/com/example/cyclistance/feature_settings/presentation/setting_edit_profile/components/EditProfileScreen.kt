package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cyclistance.common.MappingConstants.NONE_OF_THE_ABOVE_RESULT_CODE
import com.example.cyclistance.common.MappingConstants.NO_SIM_CARD_RESULT_CODE
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.EditProfileViewModel
import com.example.cyclistance.theme.*
import com.google.accompanist.permissions.*
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

    val state by editProfileViewModel.state

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()

    val toggleBottomSheet = {
        scope.launch {
            with(bottomSheetState) {
               if(isVisible) hide() else show()
            }
        }
    }


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
                    event = EditProfileEvent.EnteredPhoneNumber(
                        phoneNumber = TextFieldValue(
                            text = credential!!.id)))
            }
            NO_SIM_CARD_RESULT_CODE -> {
                Toast.makeText(context, "No SIM Card Detected", Toast.LENGTH_LONG).show()
            }
            NONE_OF_THE_ABOVE_RESULT_CODE -> {
                editProfileViewModel.onEvent(
                    event = EditProfileEvent.EnteredPhoneNumber(
                        phoneNumber = TextFieldValue(text = "")))
            }
        }
    }

    LaunchedEffect(true) {
        editProfileViewModel.eventFlow.collectLatest { event ->
            when (event) {
                is EditProfileUiEvent.ShowMappingScreen -> {
                    onPopBackStack()
                }

            }
        }
    }
    val openGalleryResultLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            editProfileViewModel.onEvent(event = EditProfileEvent.NewImageUri(uri = uri))
            uri?.let { selectedUri ->
                editProfileViewModel.onEvent(
                    event = EditProfileEvent.NewBitmapPicture(
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

            editProfileViewModel.onEvent(event = EditProfileEvent.NewBitmapPicture(bitmap = bitmap))
            editProfileViewModel.onEvent(event = EditProfileEvent.SaveImageToGallery)

        }


    val openGalleryPermissionState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE) { permissionGranted ->
            if (permissionGranted) {
                openGalleryResultLauncher.launch("image/*")
            }
        }


    val openCameraPermissionState =
        rememberMultiplePermissionsState(permissions = listOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) { permissionGranted ->

        if (permissionGranted.values.all { it }) {
            openCameraResultLauncher.launch()
        }


    }


    SelectImageBottomSheet(
        galleryButtonOnClick = {
            toggleBottomSheet()
            if (openGalleryPermissionState.status.isGranted) {
                openGalleryResultLauncher.launch("image/*")
                return@SelectImageBottomSheet
            }
            if (!openGalleryPermissionState.status.shouldShowRationale) {
                Toast.makeText(context, "Permission to open Gallery is denied.", Toast.LENGTH_LONG).show()
                return@SelectImageBottomSheet
            }
            openGalleryPermissionState.launchPermissionRequest()
        },
        cameraButtonOnClick = {
            toggleBottomSheet()
            if(openCameraPermissionState.allPermissionsGranted){
                openCameraResultLauncher.launch()
                return@SelectImageBottomSheet
            }
            if(!openCameraPermissionState.shouldShowRationale){
                Toast.makeText(context, "Permissions to open Camera is denied.",Toast.LENGTH_LONG).show()
                return@SelectImageBottomSheet
            }
            openCameraPermissionState.launchMultiplePermissionRequest()
        },
        bottomSheetScaffoldState = bottomSheetState) {

        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)) {


            val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText, progressBar) = createRefs()

            ProfilePictureArea(
                photoUrl = state.bitmap?.asImageBitmap() ?: state.photoUrl,
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




            Text(
                text = "Change Profile Photo",
                color = Blue600,
                style = MaterialTheme.typography.caption, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.constrainAs(changePhotoText) {
                    top.linkTo(profilePictureArea.bottom, margin = 12.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                })


            TextFieldInputArea(modifier = Modifier
                .constrainAs(textFieldInputArea) {
                    top.linkTo(changePhotoText.bottom, margin = 30.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.5f)
                    width = Dimension.percent(0.9f)

                },
                editProfileViewModel = editProfileViewModel,
                onPhoneTextFieldClick = {
                    simCardResultLauncher.launch(intentSender)
                })


            MappingButtonNavigation(modifier = Modifier
                .constrainAs(buttonNavigationArea) {
                    top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom, margin = 50.dp)
                    height = Dimension.percent(0.1f)
                    width = Dimension.percent(0.8f)
                },
                positiveButtonText = "Save",
                onClickCancelButton = {
                    onPopBackStack()
                },
                onClickConfirmButton = {
                    editProfileViewModel.onEvent(event = EditProfileEvent.Save)
                })

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



