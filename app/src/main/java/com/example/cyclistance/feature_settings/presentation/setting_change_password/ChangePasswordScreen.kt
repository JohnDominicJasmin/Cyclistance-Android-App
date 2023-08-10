package com.example.cyclistance.feature_settings.presentation.setting_change_password

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_mapping.presentation.common.ButtonNavigation
import com.example.cyclistance.feature_settings.presentation.setting_change_password.components.PasswordTextFieldArea
import com.example.cyclistance.theme.CyclistanceTheme

@Composable
fun ChangePasswordScreen(paddingValues: PaddingValues) {

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        color = MaterialTheme.colors.background) {

        ConstraintLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState())) {

            val (textFieldInputArea, buttonNavigationArea) = createRefs()

            PasswordTextFieldArea(modifier = Modifier.constrainAs(textFieldInputArea) {
                top.linkTo(parent.top, margin = 50.dp)
                end.linkTo(parent.end)
                start.linkTo(parent.start)
                height = Dimension.percent(0.5f)
                width = Dimension.percent(0.9f)
            })

            ButtonNavigation(
                modifier = Modifier.constrainAs(buttonNavigationArea) {
                    top.linkTo(textFieldInputArea.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.percent(0.1f)
                    width = Dimension.percent(0.8f)
                },
                positiveButtonText = "Save",
                onClickNegativeButton = {

                },
                onClickPositiveButton = {

                })

        }

    }
}

@Preview
@Composable
fun ChangePasswordPreview() {
    CyclistanceTheme(false) {
        ChangePasswordScreen(paddingValues = PaddingValues())
    }
}