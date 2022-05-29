package com.example.cyclistance.feature_settings.presentation.setting_edit_profile.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.theme.*

@Composable
fun EditProfileScreen(navigateTo: (destination: String, popUpToDestination: String?) -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)) {


        val (profilePictureArea, textFieldInputArea, buttonNavigationArea, changePhotoText) = createRefs()

        ProfilePictureArea(modifier = Modifier
            .size(105.dp)
            .constrainAs(profilePictureArea) {
                top.linkTo(parent.top, margin = 18.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)

            })


        Text(
            text = "Change Profile Photo",
            color = ChangeProfileTextColor,
            style = MaterialTheme.typography.caption, fontWeight = FontWeight.SemiBold,
        modifier = Modifier.constrainAs(changePhotoText){
            top.linkTo(profilePictureArea.bottom, margin = 4.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)

        })


        TextFieldInputArea(modifier = Modifier
            .constrainAs(textFieldInputArea) {
                top.linkTo(profilePictureArea.bottom, margin = 25.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                height = Dimension.percent(0.5f)
                width = Dimension.percent(0.9f)

            })


        MappingButtonNavigation(modifier = Modifier
            .constrainAs(buttonNavigationArea) {
                top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                height = Dimension.percent(0.1f)
                width = Dimension.percent(0.8f)
            },
            positiveButtonText = "Save",
            onClickCancelButton = {

            },
            onClickConfirmButton = {

            })


    }
}


@Preview
@Composable
fun EditProfileScreenPreview() {
    EditProfileScreen { destination, popUpToDestination ->

    }
}