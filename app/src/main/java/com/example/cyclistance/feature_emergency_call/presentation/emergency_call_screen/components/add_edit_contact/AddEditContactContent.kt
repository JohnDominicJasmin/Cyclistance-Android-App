package com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.components.add_edit_contact

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.core.utils.constants.EmergencyCallConstants.PHILIPPINE_RED_CROSS_PHOTO
import com.example.cyclistance.feature_emergency_call.domain.model.EmergencyContactModel
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.event.EmergencyCallUiEvent
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallState
import com.example.cyclistance.feature_emergency_call.presentation.emergency_call_screen.state.EmergencyCallUIState
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_user_profile.presentation.edit_profile.components.SelectImageBottomSheet
import com.example.cyclistance.theme.CyclistanceTheme
import com.example.cyclistance.top_bars.TitleTopAppBar
import com.example.cyclistance.top_bars.TopAppBarCreator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddEditContactContent(
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: ModalBottomSheetState,
    keyboardActions: KeyboardActions = KeyboardActions { },
    event: (EmergencyCallUiEvent) -> Unit,
    state: EmergencyCallState,
    name: TextFieldValue,
    phoneNumber: TextFieldValue,
    uiState: EmergencyCallUIState) {

    val isOnEditMode by remember(key1 = uiState.contactCurrentlyEditing) {
        derivedStateOf { uiState.contactCurrentlyEditing?.isEmpty() == false }
    }

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

    val selectedImage = uiState.contactCurrentlyEditing?.photo?.takeIf { it.isNotEmpty() } ?: uiState.selectedImageUri

    Dialog(
        onDismissRequest = { event(EmergencyCallUiEvent.DismissEditContactScreen) },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = false,
        )) {

        Scaffold(modifier = modifier, topBar = {
            TopAppBarCreator(
                icon = Icons.Default.Close,
                onClickIcon = { event(EmergencyCallUiEvent.DismissEditContactScreen) },
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
                        event(EmergencyCallUiEvent.ToggleBottomSheet)
                        event(EmergencyCallUiEvent.SelectImageFromGallery)
                    },
                    onClickCameraButton = {
                        event(EmergencyCallUiEvent.ToggleBottomSheet)
                        event(EmergencyCallUiEvent.OpenCamera)
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
                                selectedImage =  selectedImage,
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
                            onClickNegativeButton = { event(EmergencyCallUiEvent.CancelEditContact) },
                            onClickPositiveButton = { event(EmergencyCallUiEvent.SaveEditContact) },
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

                    }
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
            uiState = EmergencyCallUIState(),
            state = EmergencyCallState(),
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
            uiState = EmergencyCallUIState(
                contactCurrentlyEditing = EmergencyContactModel(
                    name = "Philippine Red Cross",
                    photo = PHILIPPINE_RED_CROSS_PHOTO,
                    phoneNumber = "143",
                    id = 1)),
            state = EmergencyCallState(),
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
            uiState = EmergencyCallUIState(
                contactCurrentlyEditing = EmergencyContactModel(
                    name = "Philippine Red Cross",
                    photo = PHILIPPINE_RED_CROSS_PHOTO,
                    phoneNumber = "143",
                    id = 1)),
            state = EmergencyCallState(),
            phoneNumber = TextFieldValue("143"),
            name = TextFieldValue("Philippine Red Cross")
        )
    }
}





