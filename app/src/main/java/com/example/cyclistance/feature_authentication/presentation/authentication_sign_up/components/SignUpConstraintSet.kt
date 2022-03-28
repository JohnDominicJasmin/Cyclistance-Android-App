package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem

val signUpConstraints = ConstraintSet {

    val appIcon = createRefFor(id = AuthenticationConstraintsItem.IconDisplay.layoutId)
    val topWave = createRefFor(id = AuthenticationConstraintsItem.TopWave.layoutId)
    val bottomWave = createRefFor(id = AuthenticationConstraintsItem.BottomWave.layoutId)
    val welcomeTextSection = createRefFor(id = AuthenticationConstraintsItem.WelcomeTextArea.layoutId)
    val textFieldSection = createRefFor(id = AuthenticationConstraintsItem.TextFields.layoutId)
    val signUpButton = createRefFor(id = AuthenticationConstraintsItem.SignUpButton.layoutId)
    val alreadyHaveAnAccountText = createRefFor(id = AuthenticationConstraintsItem.ClickableTextSection.layoutId)
    val progressBar = createRefFor(id = AuthenticationConstraintsItem.ProgressBar.layoutId)


    constrain(appIcon){
        top.linkTo(parent.top, margin = 12.dp)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(welcomeTextSection){
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


    constrain(topWave){
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
    constrain(signUpButton) {
        top.linkTo(textFieldSection.bottom, margin = 25.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
    constrain(alreadyHaveAnAccountText){
        top.linkTo(signUpButton.bottom, margin = 16.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
}