package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

val emailAuthConstraints = ConstraintSet {
    val emailIcon =  createRefFor(id = AuthenticationConstraintsItem.IconDisplay.layoutId)
    val textDisplay = createRefFor(id = AuthenticationConstraintsItem.WelcomeTextArea.layoutId)
    val resendEmail = createRefFor(id = AuthenticationConstraintsItem.ResendButton.layoutId)
    val verifyEmail = createRefFor(id = AuthenticationConstraintsItem.VerifyEmailButton.layoutId)
    val progressBar = createRefFor(id = AuthenticationConstraintsItem.ProgressBar.layoutId)


    constrain(emailIcon){
        top.linkTo(parent.top, margin = 10.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
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