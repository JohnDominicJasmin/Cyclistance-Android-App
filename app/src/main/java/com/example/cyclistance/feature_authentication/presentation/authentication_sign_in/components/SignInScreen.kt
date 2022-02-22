package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.AuthenticationScreen
import com.example.cyclistance.feature_authentication.presentation.common.AuthenticationConstraintsItem
import com.example.cyclistance.feature_authentication.presentation.theme.*





@Composable
fun SignInScreen(
    navController: NavController?) {


    ConstraintLayout(
        constraintSet = signInConstrains, modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {


        AppImageIcon(layoutId = AuthenticationConstraintsItem.AppIcon.layoutId)
        SignUpTextArea()
        Waves(
            topWaveLayoutId = AuthenticationConstraintsItem.TopWave.layoutId,
            bottomWaveLayoutId = AuthenticationConstraintsItem.BottomWave.layoutId
        )
        SignInTextFieldsArea()
        SignInGoogleAndFacebookButton()
        SignInButton()
        SignInClickableText(onClick = {
            navController?.navigate(AuthenticationScreen.SignUpScreen.route)
        })

    }


}


@Preview
@Composable
fun Preview() {
    SignInScreen(navController = null)
}

