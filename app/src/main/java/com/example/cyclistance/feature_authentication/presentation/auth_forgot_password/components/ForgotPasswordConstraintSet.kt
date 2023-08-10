package com.example.cyclistance.feature_authentication.presentation.auth_forgot_password.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BOTTOM_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BUTTONS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_FIELDS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_LABEL
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_SPACER_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_WAVE_ID

val forgotPasswordConstraints = ConstraintSet{
    val topWave = createRefFor(id = TOP_WAVE_ID)
    val bottomWave = createRefFor(id = BOTTOM_WAVE_ID)
    val photoDisplay = createRefFor(id = ICON_DISPLAY_ID)
    val textLabel = createRefFor(TEXT_LABEL)
    val textField = createRefFor(TEXT_FIELDS_ID)
    val topSpacer = createRefFor(id = TOP_SPACER_ID)
    val progressBar = createRefFor(id = PROGRESS_BAR_ID)
    val buttons = createRefFor(id = BUTTONS_ID)
    val dialogs = createRefFor(id = DIALOG_ID)

    constrain(topSpacer){
        top.linkTo(parent.top)
        height = Dimension.percent(0.04f)
        end.linkTo(parent.end)
        start.linkTo(parent.start)

    }

    constrain(photoDisplay) {
        top.linkTo(topSpacer.bottom, margin = 12.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.percent(0.85f)
        height = Dimension.wrapContent
    }

    constrain(textLabel) {
        top.linkTo(photoDisplay.bottom, margin = 15.dp)
        start.linkTo(parent.start)

        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }


    constrain(progressBar){
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom)
        this.centerTo(parent)
    }


    constrain(textField) {
        top.linkTo(textLabel.bottom, margin = 25.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }


    constrain(topWave) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(bottomWave) {
        bottom.linkTo(parent.bottom)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
    constrain(buttons){
        top.linkTo(textField.bottom, margin = 50.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        height = Dimension.wrapContent
        width = Dimension.percent(0.8f)
    }

    constrain(dialogs){
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom)
        this.centerTo(parent)
    }
}