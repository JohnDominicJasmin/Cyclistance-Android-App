package com.example.cyclistance.feature_authentication.presentation.auth_email.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.RESEND_EMAIL_BUTTON_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_LABEL
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.VERIFY_EMAIL_BUTTON_ID

val emailAuthConstraints = ConstraintSet {
    val emailIcon =  createRefFor(id = ICON_DISPLAY_ID)
    val textDisplay = createRefFor(id = TEXT_LABEL)
    val resendEmail = createRefFor(id = RESEND_EMAIL_BUTTON_ID)
    val verifyEmail = createRefFor(id = VERIFY_EMAIL_BUTTON_ID)
    val progressBar = createRefFor(id = PROGRESS_BAR_ID)
    val noInternetScreen = createRefFor(id = DIALOG_ID)

    constrain(emailIcon){
        top.linkTo(parent.top, margin = 10.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.percent(0.5f)
        height = Dimension.percent(0.24f)
    }

    constrain(progressBar){
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom)
        this.centerTo(parent)
    }

    constrain(noInternetScreen){
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom)
        width = Dimension.matchParent
        height = Dimension.wrapContent
    }



    constrain(textDisplay) {
        top.linkTo(emailIcon.bottom, margin = 12.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(verifyEmail){
        bottom.linkTo(resendEmail.top, margin = 5.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(resendEmail){
        bottom.linkTo(parent.bottom,margin = 10.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

}