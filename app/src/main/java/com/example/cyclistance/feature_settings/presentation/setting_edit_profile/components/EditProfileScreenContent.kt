package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.core.presentation.dialogs.no_internet_dialog.NoInternetDialog
import com.example.cyclistance.core.presentation.dialogs.permissions_dialog.DialogCameraPermission
import com.example.cyclistance.core.presentation.dialogs.permissions_dialog.DialogFilesAndMediaPermission
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.event.EditProfileUiEvent
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileState
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.state.EditProfileUiState
import com.example.cyclistance.theme.Blue600
import com.example.cyclistance.theme.CyclistanceTheme


@OptIn(ExperimentalMaterialApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewEditProfileDark() {
    CyclistanceTheme(true) {
        EditProfileScreenContent(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(isLoading = false),
            uiState = EditProfileUiState(cameraPermissionDialogVisible = true),
            bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
            name = TextFieldValue(""),
            phoneNumber = TextFieldValue(""),
            event = {}
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewEditProfileLight() {
    CyclistanceTheme(false) {
        EditProfileScreenContent(
            modifier = Modifier,
            photoUrl = "",
            state = EditProfileState(isLoading = false),
            uiState = EditProfileUiState(cameraPermissionDialogVisible = true),
            bottomSheetScaffoldState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
            name = TextFieldValue(""),
            phoneNumber = TextFieldValue(""),
            event = {}
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreenContent(
    modifier: Modifier,
    photoUrl: Any,
    state: EditProfileState = EditProfileState(),
    keyboardActions: KeyboardActions = KeyboardActions { },
    uiState: EditProfileUiState,
    bottomSheetScaffoldState: ModalBottomSheetState,
    name: TextFieldValue,
    phoneNumber: TextFieldValue,
    event: (EditProfileUiEvent) -> Unit = {}
) {


    val isUserInformationChanges by remember(
        name,
        phoneNumber,
        uiState.selectedImageUri) {
        derivedStateOf {
            name.text != state.nameSnapshot ||
            phoneNumber.text != state.phoneNumberSnapshot ||
            uiState.selectedImageUri.isNotEmpty()
        }
    }


    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colors.background) {

        SelectImageBottomSheet(
            onClickGalleryButton = {
                event(EditProfileUiEvent.ToggleBottomSheet)
                event(EditProfileUiEvent.SelectImageFromGallery)
            },
            onClickCameraButton = {
                event(EditProfileUiEvent.ToggleBottomSheet)
                event(EditProfileUiEvent.OpenCamera)
            },
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            isLoading = state.isLoading) {

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText, progressBar, noInternetDialog, permissionDialog) = createRefs()

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
                        event(EditProfileUiEvent.ToggleBottomSheet)
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
                        event(EditProfileUiEvent.ToggleBottomSheet)
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
                    onValueChangeName = { event(EditProfileUiEvent.OnChangeName(it)) },
                    onValueChangePhoneNumber = { event(EditProfileUiEvent.OnChangePhoneNumber(it)) },
                    keyboardActions = keyboardActions,
                    uiState = uiState,
                    name = name,
                    phoneNumber = phoneNumber,
                )



                ButtonNavigation(
                    modifier = Modifier
                        .constrainAs(buttonNavigationArea) {
                            top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 50.dp)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.8f)
                        },
                    positiveButtonText = "Save",
                    onClickNegativeButton = { event(EditProfileUiEvent.CancelEditProfile) },
                    onClickPositiveButton = { event(EditProfileUiEvent.ConfirmEditProfile) },
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
                        }, onDismiss = { event(EditProfileUiEvent.DismissNoInternetDialog) }
                    )

                }

                if (uiState.cameraPermissionDialogVisible) {
                    DialogCameraPermission(modifier = Modifier.constrainAs(permissionDialog) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                        centerTo(parent)
                    }, onDismiss = {
                        event(EditProfileUiEvent.DismissCameraDialog)
                    })
                }

                if (uiState.filesAndMediaDialogVisible) {
                    DialogFilesAndMediaPermission(modifier = Modifier.constrainAs(permissionDialog) {
                        end.linkTo(parent.end)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.wrapContent
                        centerTo(parent)
                    }, onDismiss = {
                        event(EditProfileUiEvent.DismissFilesAndMediaDialog)
                    })
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


