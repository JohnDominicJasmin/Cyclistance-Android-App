package com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogCameraPermission
import com.example.cyclistance.feature_dialogs.presentation.permissions_dialog.DialogFilesAndMediaPermission
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.event.AddEditContactUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_add_edit_contact.state.AddEditContactUiState
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.topAppBar.TitleTopAppBar
import com.example.cyclistance.feature_mapping.presentation.mapping_main_screen.components.topAppBar.TopAppBarCreator
import com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditContactContent(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: ModalBottomSheetState,
    keyboardActions: KeyboardActions = KeyboardActions { },
    event: (AddEditContactUiEvent) -> Unit,
    state: AddEditContactState,
    photoUrl: Any,
    uiState: AddEditContactUiState) {


    val isOnEditMode by remember(key1 = uiState.idCurrentlyEditing) {
        derivedStateOf { uiState.idCurrentlyEditing != null }
    }
    val isUserInformationChanges by remember(
        uiState.name,
        uiState.phoneNumber,
        uiState.selectedImageUri) {
        derivedStateOf {
            uiState.name.text != state.nameSnapshot ||
            uiState.phoneNumber.text != state.phoneNumberSnapshot ||
            uiState.selectedImageUri.isNotEmpty()
        }
    }


    Scaffold(modifier = modifier, topBar = {
        TopAppBarCreator(
            icon = Icons.Default.Close,
            onClickIcon = { event(AddEditContactUiEvent.CloseEditContactScreen) },
            topAppBarTitle = {
                TitleTopAppBar(title = if (isOnEditMode) "Edit Contact" else "New Contact")
            })

    }) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(), color = MaterialTheme.colors.background) {

            SelectImageBottomSheet(
                onClickGalleryButton = {
                    event(AddEditContactUiEvent.ToggleBottomSheet)
                    event(AddEditContactUiEvent.SelectImageFromGallery)
                },
                onClickCameraButton = {
                    event(AddEditContactUiEvent.ToggleBottomSheet)
                    event(AddEditContactUiEvent.OpenCamera)
                },
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                isLoading = state.isLoading) {

                ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                    val (addPhoto, content, button, permissionDialog, progressBar) = createRefs()

                    Column(
                        modifier = Modifier
                            .wrapContentSize()
                            .constrainAs(addPhoto) {
                                top.linkTo(parent.top, margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        verticalArrangement = Arrangement.spacedBy(
                            7.dp,
                            alignment = Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally) {

                        AddEditPhotoSection(
                            isOnEditMode = isOnEditMode,
                            photoUrl = photoUrl,
                            event = event)

                        Text(
                            text = if (isOnEditMode) "Change Photo" else "Add Photo",
                            color = MaterialTheme.colors.onBackground,
                            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                        )
                    }


                    AddEditContextTextFieldSection(
                        modifier = Modifier.constrainAs(content) {
                            top.linkTo(addPhoto.bottom, margin = 25.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.9f)
                        },
                        uiState = uiState,
                        state = state,
                        event = event,
                        keyboardActions = keyboardActions)


                    ButtonNavigation(
                        modifier = Modifier.constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                            width = Dimension.percent(0.8f)
                        },
                        positiveButtonText = "Save",
                        onClickCancelButton = { event(AddEditContactUiEvent.CancelEditContact) },
                        onClickConfirmButton = { event(AddEditContactUiEvent.SaveEditContact) },
                        negativeButtonEnabled = !state.isLoading,
                        positiveButtonEnabled = !state.isLoading && isUserInformationChanges
                    )




                    if (uiState.cameraPermissionDialogVisible) {
                        DialogCameraPermission(modifier = Modifier.constrainAs(permissionDialog) {
                            end.linkTo(parent.end)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.wrapContent
                            centerTo(parent)
                        }, onDismiss = {
                            event(AddEditContactUiEvent.DismissCameraDialog)
                        })
                    }

                    if (uiState.filesAndMediaDialogVisible) {
                        DialogFilesAndMediaPermission(
                            modifier = Modifier.constrainAs(
                                permissionDialog) {
                                end.linkTo(parent.end)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                                height = Dimension.wrapContent
                                centerTo(parent)
                            }, onDismiss = {
                                event(AddEditContactUiEvent.DismissFilesAndMediaDialog)
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

}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewAddNewContactContentDarkEditMode() {
    CyclistanceTheme(darkTheme = true) {
        AddEditContactContent(
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Expanded),
            event = {},
            uiState = AddEditContactUiState(idCurrentlyEditing = "1"),
            state = AddEditContactState(),
            photoUrl = PHILIPPINE_RED_CROSS_PHOTO)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewAddNewContactContentDark() {
    CyclistanceTheme(darkTheme = true) {
        AddEditContactContent(
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Hidden),
            event = {},
            uiState = AddEditContactUiState(),
            state = AddEditContactState(),
            photoUrl = PHILIPPINE_RED_CROSS_PHOTO)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun PreviewAddNewContactContentLight() {
    CyclistanceTheme(darkTheme = false) {
        AddEditContactContent(
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Expanded),
            event = {},
            uiState = AddEditContactUiState(),
            state = AddEditContactState(), photoUrl = PHILIPPINE_RED_CROSS_PHOTO)
    }
}




