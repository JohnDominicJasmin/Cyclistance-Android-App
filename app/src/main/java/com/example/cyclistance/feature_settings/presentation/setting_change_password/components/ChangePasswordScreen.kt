package com.example.cyclistance.feature_settings.presentation.setting_change_password.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_main_screen.presentation.common.MappingButtonNavigation
import com.example.cyclistance.theme.BackgroundColor

@Preview
@Composable
fun ChangePasswordScreen() {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(BackgroundColor)) {

        val (textFieldInputArea, buttonNavigationArea) = createRefs()

        PasswordTextFieldArea(modifier = Modifier.constrainAs(textFieldInputArea) {
            top.linkTo(parent.top, margin = 50.dp)
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            height = Dimension.percent(0.5f)
            width = Dimension.percent(0.9f)
        })

        MappingButtonNavigation(modifier = Modifier.constrainAs(buttonNavigationArea) {
            top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
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