package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem


val signInConstraints = ConstraintSet {
    val appIcon = createRefFor(id = AuthenticationConstraintsItem.IconDisplay.layoutId)
    val topWave = createRefFor(id = AuthenticationConstraintsItem.TopWave.layoutId)
    val bottomWave = createRefFor(id = AuthenticationConstraintsItem.BottomWave.layoutId)
    val welcomeTextSection = createRefFor(id = AuthenticationConstraintsItem.WelcomeTextArea.layoutId)
    val textFieldSection = createRefFor(id = AuthenticationConstraintsItem.TextFields.layoutId)
    val facebookAndGoogle = createRefFor(id = AuthenticationConstraintsItem.OtherSignIns.layoutId)
    val signInButton = createRefFor(id = AuthenticationConstraintsItem.SignInButton.layoutId)
    val dontHaveAccountText = createRefFor(id = AuthenticationConstraintsItem.ClickableTextSection.layoutId)
    val progressBar = createRefFor(id = AuthenticationConstraintsItem.ProgressBar.layoutId)
    val noInternetScreen = createRefFor(id = AuthenticationConstraintsItem.NoInternetScreen.layoutId)
    val topSpacer = createRefFor(id = AuthenticationConstraintsItem.TopSpacer.layoutId)

    constrain(topSpacer){
        top.linkTo(parent.top)
        height = Dimension.percent(0.04f)
        end.linkTo(parent.end)
        start.linkTo(parent.start)

    }


    constrain(appIcon) {
        top.linkTo(topSpacer.bottom, margin = 12.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.percent(0.3f)
        height = Dimension.wrapContent
    }

    constrain(welcomeTextSection) {
        top.linkTo(appIcon.bottom, margin = 15.dp)
        end.linkTo(parent.end)
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

    constrain(noInternetScreen){
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        bottom.linkTo(parent.bottom)
        width = Dimension.matchParent
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

    constrain(textFieldSection) {
        top.linkTo(welcomeTextSection.bottom, margin = 20.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(facebookAndGoogle) {
        top.linkTo(textFieldSection.bottom, margin = 15.dp)
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