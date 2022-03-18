package com.example.cyclistance.feature_authentication.presentation.authentication_email.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.example.cyclistance.feature_authentication.presentation.theme.BackgroundColor

@Composable
fun EmailAuthScreen(navController: NavController?) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)) {
        ConstraintLayout(
            constraintSet = emailAuthConstraints,
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .background(BackgroundColor)) {

            EmailIcon()
            EmailAuthTextStatus(email = "johndominicjasmin@gmail.com")
            EmailAuthResendButton(onClick = {

            })


        }
    }
}

@Preview
@Composable
fun PreviewEmail() {
    EmailAuthScreen(null)
}