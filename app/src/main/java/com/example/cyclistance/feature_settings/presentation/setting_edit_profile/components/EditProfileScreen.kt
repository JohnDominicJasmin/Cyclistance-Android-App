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
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun EditProfileScreen(
    editProfileViewModel: EditProfileViewModel = hiltViewModel(),
    onPopBackStack: () -> Unit,
    navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

    val state by editProfileViewModel.state

    val context = LocalContext.current
    val hintRequest = HintRequest.Builder()
        .setPhoneNumberIdentifierSupported(true)
        .build()

    val simCardIntent = Credentials.getClient(context).getHintPickerIntent(hintRequest)
    val intentSender = IntentSenderRequest.Builder(simCardIntent.intentSender).build()

    val simCardResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->

        when (result.resultCode) {
            Activity.RESULT_OK -> {
                val credential: Credential? = result.data?.getParcelableExtra(Credential.EXTRA_KEY)
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
                        phoneNumber = TextFieldValue(
                            text = "")))
            }
        }
    }
    LaunchedEffect(true){
        editProfileViewModel.eventFlow.collectLatest { event ->
            when(event){
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
                                    ImageDecoder.createSource(context.contentResolver, selectedUri)
                                ImageDecoder.decodeBitmap(source)
                            }
                        }
                    ))
            }
        }


    val permissionState =
        rememberPermissionState(permission = Manifest.permission.READ_EXTERNAL_STORAGE) { permissionGranted ->
            if (permissionGranted) {
                openGalleryResultLauncher.launch("image/*")
            }
        }

//todo: add progress bar


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)) {


        val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText) = createRefs()

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
                if (permissionState.status.isGranted) {
                    openGalleryResultLauncher.launch("image/*")
                    return@ProfilePictureArea
                }
                if (!permissionState.status.shouldShowRationale) {
                    Toast.makeText(context, "Permission is denied.", Toast.LENGTH_SHORT).show()
                    return@ProfilePictureArea
                }

                permissionState.launchPermissionRequest()
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

//todo: add dialog when document changed and user is exiting without saving
        //todo: add navigation for settings 
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
                editProfileViewModel.onEvent(event = EditProfileEvent.SaveProfile)
            })


    }
}

