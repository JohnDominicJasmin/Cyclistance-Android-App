package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension


val constrains = ConstraintSet {
    val appIcon = createRefFor(id = SignInConstraintsItem.AppIcon.layoutId)
    val topWave = createRefFor(id = SignInConstraintsItem.TopWave.layoutId)
    val bottomWave = createRefFor(id = SignInConstraintsItem.BottomWave.layoutId)
    val welcomeTextArea = createRefFor(id = SignInConstraintsItem.WelcomeTextArea.layoutId)
    val textFieldArea = createRefFor(id = SignInConstraintsItem.TextFields.layoutId)
    val facebookAndGoogle = createRefFor(id = SignInConstraintsItem.OtherSignIns.layoutId)
    val signInButton = createRefFor(id = SignInConstraintsItem.SignInButton.layoutId)
    val dontHaveAccountText = createRefFor(id = SignInConstraintsItem.DontHaveAccountText.layoutId)


    constrain(appIcon) {
        top.linkTo(parent.top, margin = 15.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(welcomeTextArea) {
        top.linkTo(appIcon.bottom, margin = 10.dp)
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

    constrain(textFieldArea) {
        top.linkTo(welcomeTextArea.bottom, margin = 27.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(facebookAndGoogle) {
        top.linkTo(textFieldArea.bottom, margin = 4.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(signInButton) {
        top.linkTo(facebookAndGoogle.bottom, margin = 10.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
    constrain(dontHaveAccountText){
        top.linkTo(signInButton.bottom, margin = 16.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
}