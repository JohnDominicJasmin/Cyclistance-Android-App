package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BOTTOM_WAVE_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.BUTTONS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DIALOG_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.DONT_HAVE_ACCOUNT_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.FB_AND_GOOGLE_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.ICON_DISPLAY_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.PROGRESS_BAR_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_FIELDS_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TEXT_LABEL
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_SPACER_ID
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstrains.TOP_WAVE_ID


val signInConstraints = ConstraintSet {
    val appIcon = createRefFor(id = ICON_DISPLAY_ID)
    val topWave = createRefFor(id = TOP_WAVE_ID)
    val bottomWave = createRefFor(id = BOTTOM_WAVE_ID)
    val welcomeTextSection = createRefFor(id = TEXT_LABEL)
    val textFieldSection = createRefFor(id = TEXT_FIELDS_ID)
    val facebookAndGoogle = createRefFor(id = FB_AND_GOOGLE_ID)
    val buttons = createRefFor(id = BUTTONS_ID)
    val dontHaveAccountText = createRefFor(id = DONT_HAVE_ACCOUNT_ID)
    val progressBar = createRefFor(id = PROGRESS_BAR_ID)
    val noInternetScreen = createRefFor(id = DIALOG_ID)
    val topSpacer = createRefFor(id = TOP_SPACER_ID)

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

    constrain(buttons) {
        top.linkTo(facebookAndGoogle.bottom, margin = 10.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
    constrain(dontHaveAccountText){
        top.linkTo(buttons.bottom, margin = 16.dp)
        end.linkTo(parent.end)
        start.linkTo(parent.start)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }
}