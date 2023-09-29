package com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.core.presentation.dialogs.permissions_dialog.DialogCameraPermission
import com.example.cyclistance.core.presentation.dialogs.permissions_dialog.DialogFilesAndMediaPermission
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.event.AddEditContactUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactState
import com.example.cyclistance.feature_emergency_call.presentation.add_edit_contact.state.AddEditContactUiState
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.theme.CyclistanceTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditContactContent(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: ModalBottomSheetState,
    keyboardActions: KeyboardActions = KeyboardActions { },
    event: (AddEditContactUiEvent) -> Unit,
    state: AddEditContactState,
    name: TextFieldValue,
    phoneNumber: TextFieldValue,
    uiState: AddEditContactUiState) {

    val isOnEditMode by remember(key1 = state.emergencyContact) {
        derivedStateOf { state.emergencyContact != null }
    }

    val isUserInformationChanges by remember(
        name,
        phoneNumber,
        uiState.selectedImageUri) {
        derivedStateOf {
            name.text != state.nameSnapshot ||
            phoneNumber.text != state.phoneNumberSnapshot ||
            uiState.selectedImageUri?.isNotEmpty() == true
        }
    }

    val selectedImage =
        state.emergencyContact?.photo?.takeIf { it.isNotEmpty() } ?: uiState.selectedImageUri


    Surface(
        modifier = modifier
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

                val (addPhoto, content, button, progressBar) = createRefs()

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
                        selectedImage = selectedImage ?: "",
                        event = event)

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
                    keyboardActions = keyboardActions,
                    name = name,
                    phoneNumber = phoneNumber)


                ButtonNavigation(
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(content.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        width = Dimension.percent(0.8f)
                    },
                    positiveButtonText = "Save",
                    onClickNegativeButton = { event(AddEditContactUiEvent.CancelEditContact) },
                    onClickPositiveButton = { event(AddEditContactUiEvent.SaveEditContact) },
                    negativeButtonEnabled = !state.isLoading,
                    positiveButtonEnabled = !state.isLoading && isUserInformationChanges
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


                if (uiState.cameraPermissionDialogVisible) {
                    DialogCameraPermission(onDismiss = {
                        event(AddEditContactUiEvent.DismissCameraDialog)
                    })
                }

                if (uiState.filesAndMediaDialogVisible) {
                    DialogFilesAndMediaPermission(onDismiss = {
                        event(AddEditContactUiEvent.DismissFilesAndMediaDialog)
                    })
                }


            }
        }

    }

}


@OptIn(ExperimentalMaterialApi::class)
@Preview(device = "id:Galaxy Nexus")
@Composable
fun PreviewAddNewContactContentDarkEditMode() {
    CyclistanceTheme(darkTheme = true) {
        AddEditContactContent(
            bottomSheetScaffoldState = rememberModalBottomSheetState(
                ModalBottomSheetValue.Expanded),
            event = {},
            uiState = AddEditContactUiState(),
            state = AddEditContactState(),
            phoneNumber = TextFieldValue("143"),
            name = TextFieldValue("Philippine Red Cross"))
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
            phoneNumber = TextFieldValue("143"),
            name = TextFieldValue("Philippine Red Cross")
        )
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
            state = AddEditContactState(),
            phoneNumber = TextFieldValue("143"),
            name = TextFieldValue("Philippine Red Cross")
        )
    }
}





