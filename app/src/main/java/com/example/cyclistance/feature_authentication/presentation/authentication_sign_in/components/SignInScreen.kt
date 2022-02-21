package com.example.cyclistance.feature_authentication.presentation.authentication_sign_in.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.theme.*





@Composable
fun SignInScreen(
    navController: NavController) {


    ConstraintLayout(
        constraintSet = constrains, modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {


        AppImageIcon()
        TextArea()
        Waves()
        TextFieldsArea()
        GoogleAndFacebookSignIn()
        SignInButton()
        DontHaveAccountText()

    }


}




