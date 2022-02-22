package com.example.cyclistance.feature_authentication.presentation.authentication_sign_up.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.AuthenticationScreen
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.AppImageIcon
import com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components.Waves
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor

@Composable
fun SignUpScreen(navController: NavController?) {


    ConstraintLayout(
        constraintSet = signUpConstraints, modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {

        AppImageIcon(layoutId = AuthenticationConstraintsItem.AppIcon.layoutId)
        SignUpTextArea()

        Waves(
            topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
            bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
        )

        SignUpTextFields()
        SignUpButton()
        SignUpClickableText() {
            navController?.navigate(AuthenticationScreen.SignInScreen.route)
        }


    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = null)
}